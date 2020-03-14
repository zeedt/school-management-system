package com.zeed.assignment.sms.service;

import com.zeed.assignment.sms.apimodels.SignRequest;
import com.zeed.assignment.sms.apimodels.SignupResponse;
import com.zeed.assignment.sms.models.RegistrationInfo;
import com.zeed.assignment.sms.models.Result;
import com.zeed.assignment.sms.models.Student;
import com.zeed.assignment.sms.repository.RegistrationInfoRepository;
import com.zeed.assignment.sms.repository.ResultRepository;
import com.zeed.assignment.sms.repository.StudentRepository;
import com.zeed.assignment.sms.security.UserDetailsTokenEnvelope;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    RegistrationInfoRepository registrationInfoRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ResultRepository resultRepository;

    private Logger logger = LoggerFactory.getLogger(StudentService.class.getName());

    public String validateSignupDetails(SignRequest signRequest) {
        if (signRequest == null) {
            return "invalid request";
        }

        if (StringUtils.isEmpty(signRequest.getFirstName()) || signRequest.getFirstName().trim().length() < 3) {
            return "First name must have atleast 3 characters";
        }
        if (StringUtils.isEmpty(signRequest.getLastName()) || signRequest.getLastName().trim().length() < 3) {
            return "Last name must have atleast 3 characters";
        }
        if (StringUtils.isEmpty(signRequest.getEmail()) || !signRequest.getEmail().trim().contains("@")) {
            return "Invaliid email";
        }
        if (StringUtils.isEmpty(signRequest.getRegNo())) {
            return "Invalid registration number";
        }

        if (signRequest.getaClass() == null) {
            return "Invalid class selected";
        }

        if (signRequest.getMultipartFile() == null) {
            return "Passport must be uploaded";
        }

        return null;

    }

    @Transactional
    public SignupResponse registerUser(SignRequest signRequest) throws IOException {
        SignupResponse signupResponse = new SignupResponse();
        RegistrationInfo registrationInfo = registrationInfoRepository.findOneByRegistrationNo(signRequest.getRegNo().trim());

        if (registrationInfo == null) {
            signupResponse.setSuccessful(false);
            signupResponse.setMessage("Registration number not found");
            return signupResponse;
        }

        Student existingStudent = studentRepository.findFirstByRegistrationInfo_RegistrationNo(signRequest.getRegNo().trim());

        if (existingStudent != null) {
            signupResponse.setSuccessful(false);
            signupResponse.setMessage("Registration number has been registered by another student");
            return signupResponse;
        }

        existingStudent = studentRepository.findByEmail(signRequest.getEmail());

        if (existingStudent != null) {
            signupResponse.setSuccessful(false);
            signupResponse.setMessage("Email already exists");
            return signupResponse;
        }

        String base64Image = getBase64StringFromMultipartFile(signRequest.getMultipartFile());

        Student student = new Student();
        student.setRegistrationInfo(registrationInfo);
        student.setFirstName(signRequest.getFirstName());
        student.setEmail(signRequest.getEmail());
        student.setLastname(signRequest.getLastName());
        student.setPin(generatePin(registrationInfo.getId()));
        student.setSignupDate(new Date());
        student.setBase64Image(base64Image);
        studentRepository.save(student);

        if (registrationInfo.getaClass() != signRequest.getaClass()) {
            registrationInfo.setaClass(signRequest.getaClass());
            registrationInfoRepository.save(registrationInfo);
        }

        signupResponse.setMessage(String.format("Registartion successful. Login pin is %s ", student.getPin()));
        signupResponse.setPin(student.getPin());
        signupResponse.setSuccessful(true);
        return signupResponse;

    }



    public Page<Student> fetchStudentsByPage(Integer pageNo, Integer pageSize) {

        try {
            return studentRepository.findAllByIdNotNull(new PageRequest(pageNo, pageSize, Sort.Direction.DESC, "id"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching details due to ", e);
            return new PageImpl<>(new ArrayList<>());
        }
    }

    public Page<RegistrationInfo> fetchRegistrationInfoByPage(Integer pageNo, Integer pageSize) {

        try {
            return registrationInfoRepository.findAllByIdNotNull(new PageRequest(pageNo, pageSize, Sort.Direction.DESC, "id"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching registration details due to ", e);
            return new PageImpl<>(new ArrayList<>());
        }
    }

    public List<Result> fetchCurrentStudentResult() {

        try {
            Student student = getCurrentStudent();
            return resultRepository.findAllByStudentRegistrationNumber(student.getRegistrationInfo().getRegistrationNo());
        } catch (Exception e) {
            logger.error("Error occurred while fetching registration details due to ", e);
            return new ArrayList<>();
        }
    }

    public  String generatePin(Long id) {
        SecureRandom secureRandom = new SecureRandom();
        int randomNumber = secureRandom.nextInt(1000001);
        randomNumber = Math.toIntExact(randomNumber + id);
        String token = String.valueOf(randomNumber);
        return token;
    }

    public Student getCurrentStudent() {
        UserDetailsTokenEnvelope userDetailsTokenEnvelope = (UserDetailsTokenEnvelope) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetailsTokenEnvelope.student;
    }

    public String getBase64StringFromMultipartFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) {
            return null;
        }

        String contentType = multipartFile.getContentType();
        long size = multipartFile.getSize();
        byte[] contentBytes = multipartFile.getBytes();
        String base64String = "data:image/png;base64," + Base64Utils.encodeToString(contentBytes);

        return base64String;

    }
}
