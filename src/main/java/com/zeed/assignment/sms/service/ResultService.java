package com.zeed.assignment.sms.service;

import com.zeed.assignment.sms.enums.Class;
import com.zeed.assignment.sms.models.Result;
import com.zeed.assignment.sms.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    public Page<Result> getResultsByClassAndSubjectCode(Class aClass, String subjectCode, Integer pageNo, Integer pageSize) {

        pageNo = (pageNo == null) ? 0 : pageNo;
        pageSize = (pageSize == null) ? 1 : pageSize;

        Page<Result> resultPage = resultRepository.findAllBySubject_AClassAndSubject_SubjectCode(aClass,subjectCode, new PageRequest(pageNo, pageSize, Sort.Direction.DESC, "id"));

        if (resultPage == null) {
            return new PageImpl<>(new ArrayList<Result>());
        }

        return resultPage;
    }


}
