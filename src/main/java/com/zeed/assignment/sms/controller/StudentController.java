package com.zeed.assignment.sms.controller;

import com.zeed.assignment.sms.apimodels.SignRequest;
import com.zeed.assignment.sms.apimodels.SignupResponse;
import com.zeed.assignment.sms.enums.Class;
import com.zeed.assignment.sms.models.RegistrationInfo;
import com.zeed.assignment.sms.models.Result;
import com.zeed.assignment.sms.models.Student;
import com.zeed.assignment.sms.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/")
public class StudentController {

    @Autowired
    private StudentService studentService;

    private Logger logger = LoggerFactory.getLogger(StudentController.class.getName());

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("signup")
    public String signup() {
        return "signup";
    }

    @GetMapping ("/logout")
    public String logout(Principal principal, HttpServletRequest servletRequest) {
        try {
            SecurityContextHolder.getContext().setAuthentication(null);
            } catch (Exception e) {
            logger.error("Error occured while logging out user due to ", e);
        }
        return "login";
    }

    @GetMapping("")
    public ModelAndView loadHomePage(Principal principal) {
        Collection<? extends GrantedAuthority> authorities =  SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean isAdmin = false;
        for (GrantedAuthority auth : authorities) {
            if (auth.getAuthority().equalsIgnoreCase("ADMIN")) {
                isAdmin = true;
            }
        }

        ModelAndView modelAndView = new ModelAndView();
        if (!isAdmin) {
            modelAndView.setViewName("student-home");
            Student student = studentService.getCurrentStudent();
            modelAndView.addObject("student", student);
            return modelAndView;
        }
        modelAndView.setViewName("admin-home");
        return modelAndView;
    }

    @PostMapping("/signup")
    @ResponseBody
    public SignupResponse signup(@RequestParam("image") MultipartFile file,
                                 RedirectAttributes redirectAttributes, @RequestParam("regNo") String regNo, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("email") String email, @RequestParam("sClass") Class aClass) {
        SignupResponse signupResponse = new SignupResponse();
        try {

            SignRequest signRequest = new SignRequest();
            signRequest.setaClass(aClass);
            signRequest.setRegNo(regNo);
            signRequest.setEmail(email);
            signRequest.setFirstName(firstName);
            signRequest.setLastName(lastName);
            signRequest.setMultipartFile(file);

            String validationMessage = studentService.validateSignupDetails(signRequest);

            if (validationMessage != null) {
                signupResponse.setMessage(validationMessage);
                signupResponse.setSuccessful(false);
                return signupResponse;
            }

            return studentService.registerUser(signRequest);
        } catch (Exception e) {
            logger.error("Error occurred due to ", e);
            signupResponse.setSuccessful(false);
            signupResponse.setMessage("System error occurred while registering user");
            return signupResponse;
        }

    }


    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @GetMapping("/studentlist")
    public ModelAndView studentList(@RequestParam(value = "pageNo", required = false) Integer pageNo , @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        ModelAndView modelAndView = new ModelAndView();

        pageNo = (pageNo == null) ? 0 : pageNo;
        pageSize = (pageSize == null) ? 15 : pageSize;

        Page<Student> studentPage = studentService.fetchStudentsByPage(pageNo, pageSize);

        modelAndView.addObject("pageNo", pageNo);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("studentList", studentPage.getContent());
        modelAndView.addObject("totalElements", studentPage.getTotalElements());
        modelAndView.addObject("totalPageNo", studentPage.getTotalPages());

        modelAndView.setViewName("student-list");

        return modelAndView;
    }

    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @GetMapping("registrationlist")
    public ModelAndView getregistrationDetails(@RequestParam(value = "pageNo", required = false) Integer pageNo , @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        ModelAndView modelAndView = new ModelAndView();

        pageNo = (pageNo == null) ? 0 : pageNo;
        pageSize = (pageSize == null) ? 15 : pageSize;

        Page<RegistrationInfo> registrationInfoPage = studentService.fetchRegistrationInfoByPage(pageNo, pageSize);

        modelAndView.addObject("pageNo", pageNo);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("registrationList", registrationInfoPage.getContent());
        modelAndView.addObject("totalElements", registrationInfoPage.getTotalElements());
        modelAndView.addObject("totalPageNo", registrationInfoPage.getTotalPages());

        modelAndView.setViewName("registration-list");

        return modelAndView;

    }
    @PreAuthorize(value = "hasAnyAuthority('STUDENT')")
    @GetMapping("/my-result")
    public ModelAndView getStudentResult() {
        ModelAndView modelAndView = new ModelAndView();
        List<Result> resultList = studentService.fetchCurrentStudentResult();

        modelAndView.addObject("resultLists", resultList);

        modelAndView.setViewName("student-result");

        return modelAndView;

    }


}
