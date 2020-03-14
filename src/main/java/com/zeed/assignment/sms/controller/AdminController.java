package com.zeed.assignment.sms.controller;

import com.zeed.assignment.sms.apimodels.RegistrationDetailsUploadResponseModel;
import com.zeed.assignment.sms.apimodels.ResultUploadResponseModel;
import com.zeed.assignment.sms.enums.Class;
import com.zeed.assignment.sms.models.RegistrationInfo;
import com.zeed.assignment.sms.models.Result;
import com.zeed.assignment.sms.models.Subject;
import com.zeed.assignment.sms.repository.RegistrationInfoRepository;
import com.zeed.assignment.sms.repository.ResultRepository;
import com.zeed.assignment.sms.repository.SubjectRepository;
import com.zeed.assignment.sms.service.ResultService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class AdminController {

    @Autowired
    private RegistrationInfoRepository registrationInfoRepository;

    private Logger logger = LoggerFactory.getLogger(AdminController.class.getName());

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ResultService resultService;

    @ResponseBody
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @RequestMapping(value = "/upload-bulk-registration-details", method = RequestMethod.POST)
    public RegistrationDetailsUploadResponseModel uploadregistrationDetails(@RequestParam("file") MultipartFile file,
                                                                            RedirectAttributes redirectAttributes) throws IOException {

        RegistrationDetailsUploadResponseModel registrationDetailsUploadResponseModel = new RegistrationDetailsUploadResponseModel();

        if (file.isEmpty()) {
            registrationDetailsUploadResponseModel.setMessage("No file selected");
            return registrationDetailsUploadResponseModel;
        }


        List<Integer> failureRowNumber = new ArrayList<>();
        List<String> failureRowReason = new ArrayList<>();
        Integer successfulCount = 0;
        Integer failureCount = 0;
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());

            Sheet sheet = workbook.getSheetAt(0);

            for (int i=1;i<sheet.getPhysicalNumberOfRows();i++) {

                try {
                    if (sheet != null) {
                        Row row = sheet.getRow(i);
                        String registrationNumber = row.getCell(0).getStringCellValue();
                        String firstName = row.getCell(1).getStringCellValue();
                        String lastName = row.getCell(2).getStringCellValue();
                        Class aclass = Class.valueOf(row.getCell(3).getStringCellValue());

                        if (StringUtils.isEmpty(registrationNumber) || StringUtils.isEmpty(firstName) || StringUtils.isEmpty(lastName) || aclass == null ) {
                            failureRowNumber.add(i+1);
                            failureRowReason.add("First name, Last name, registration number and class cannot be blank");
                            failureCount +=1;
                            continue;
                        }

                        RegistrationInfo existingRecord = registrationInfoRepository.findOneByRegistrationNo(registrationNumber.trim());

                        if (existingRecord != null) {
                            failureRowNumber.add(i+1);
                            failureRowReason.add(String.format("Registration number %s already exists on the platform", registrationNumber));
                            failureCount +=1;
                            continue;
                        }

                        RegistrationInfo registrationInfo = new RegistrationInfo();
                        registrationInfo.setFirstName(firstName);
                        registrationInfo.setLastName(lastName);
                        registrationInfo.setaClass(aclass);
                        registrationInfo.setRegistrationNo(registrationNumber);
                        registrationInfo.setUploadedDate(new Date());
                        registrationInfoRepository.save(registrationInfo);
                        successfulCount +=1;


                    }
                } catch (Exception e) {
                    logger.error("Error occurred due to ", e);
                    failureRowNumber.add(i+1);
                    failureRowReason.add(String.format("System error occurred due to : %s", e.getMessage()));
                    failureCount +=1;
                }

            }
        } catch (Exception e) {
            logger.error("Error occurred due to ", e);
            registrationDetailsUploadResponseModel.setMessage("Error occurred while processing. Ensure you select .xlsx file");
            return registrationDetailsUploadResponseModel;
        }

        registrationDetailsUploadResponseModel.setFailureCount(failureCount);
        registrationDetailsUploadResponseModel.setSuccessfulCount(successfulCount);
        registrationDetailsUploadResponseModel.setMessage(String.format("%d record(s) are succcessful and %d record(s) failed", successfulCount, failureCount));
        registrationDetailsUploadResponseModel.setFailureRows(failureRowNumber);
        registrationDetailsUploadResponseModel.setFailureReasons(failureRowReason);

        return registrationDetailsUploadResponseModel;

    }

    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @GetMapping("/upload-result")
    public String uploadResultPage() {
        return "result-upload";
    }


    @ResponseBody
    @RequestMapping(value = "/process/upload-result", method = RequestMethod.POST)
    public ResultUploadResponseModel uploadregistrationDetails(@RequestParam("file") MultipartFile file,
                                                                            RedirectAttributes redirectAttributes, @RequestParam("sClass") Class sClass, @RequestParam("subjectCode") String subjectCode) throws IOException {
        ResultUploadResponseModel resultUploadResponseModel = new ResultUploadResponseModel();

        Subject subject = subjectRepository.findTopByAClassAndSubjectCode(sClass, subjectCode);

        if (subject == null) {
            resultUploadResponseModel.setMessage("Subject not found for the specified class");
            return resultUploadResponseModel;
        }



        if (file.isEmpty()) {
            resultUploadResponseModel.setMessage("No file selected");
            return resultUploadResponseModel;
        }

        List<Integer> failureRowNumber = new ArrayList<>();
        List<String> failureRowReason = new ArrayList<>();
        Integer successfulCount = 0;
        Integer failureCount = 0;
        try {

            Workbook workbook = new XSSFWorkbook(file.getInputStream());

            Sheet sheet = workbook.getSheetAt(0);

            for (int i=1;i<sheet.getPhysicalNumberOfRows();i++) {
                try {

                    if (sheet != null) {
                        Row row = sheet.getRow(i);
                        String registrationNumber = row.getCell(0).getStringCellValue();
                        Double score = row.getCell(1).getNumericCellValue();
                        int intScore = score.intValue();

                        if (StringUtils.isEmpty(registrationNumber) || intScore < 0 || intScore > 100 ) {
                            failureRowNumber.add(i+1);
                            failureRowReason.add("Registration number cannot be blank. Score must also b between 0 and 100");
                            failureCount +=1;
                            continue;
                        }

                        Result existingResult = resultRepository.findTopBySubject_SubjectCodeAndStudentRegistrationNumber(subjectCode,registrationNumber);

                        if (existingResult != null) {
                            failureRowNumber.add(i+1);
                            failureRowReason.add("Result already uploaded for this candidate");
                            failureCount +=1;
                            continue;
                        }

                        Result result = new Result();
                        result.setScore(intScore);
                        result.setStudentRegistrationNumber(registrationNumber);
                        result.setSubject(subject);
                        result.setUploadedDate(new Date());
                        resultRepository.save(result);
                        successfulCount += 1;

                    }

                } catch (Exception e) {
                    logger.error("Error occurred due to ", e);
                    failureRowNumber.add(i+1);
                    failureRowReason.add(String.format("System error occurred due to : %s", e.getMessage()));
                    failureCount +=1;
                }
            }



        } catch (Exception e) {

            logger.error("Error occurred due to ", e);
            resultUploadResponseModel.setMessage("Error occurred while processing. Ensure you select .xlsx file");
            return resultUploadResponseModel;
        }


        resultUploadResponseModel.setFailureCount(failureCount);
        resultUploadResponseModel.setSuccessfulCount(successfulCount);
        resultUploadResponseModel.setMessage(String.format("%d record(s) are succcessful and %d record(s) failed", successfulCount, failureCount));
        resultUploadResponseModel.setFailureRows(failureRowNumber);
        resultUploadResponseModel.setFailureReasons(failureRowReason);

        return resultUploadResponseModel;

    }

    @GetMapping("/view-results")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ModelAndView viewUploadedResults(@RequestParam(value = "subjectCode", required = false) String subjectCode, @RequestParam(value = "sClass", required = false) Class sClass, @RequestParam(value = "pageSize", required = false) Integer pageSize, @RequestParam(value = "pageNo", required = false) Integer pageNo) {

        ModelAndView modelAndView = new ModelAndView();

        if (StringUtils.isEmpty(subjectCode) || sClass == null) {
            modelAndView.setViewName("view-results");
            modelAndView.addObject("sClass","");
            return modelAndView;
        }

        Page<Result> resultPage = resultService.getResultsByClassAndSubjectCode(sClass, subjectCode, pageNo, pageSize);

        modelAndView.addObject("loaded",true);
        modelAndView.addObject("subjectCode",subjectCode);
        modelAndView.addObject("sClass",sClass);
        modelAndView.addObject("resultLists",resultPage.getContent());
        modelAndView.addObject("totalElements",resultPage.getTotalElements());
        modelAndView.addObject("pageNo",pageNo);
        modelAndView.addObject("totalPageNo",resultPage.getTotalPages());

        return modelAndView;
    }



    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

}
