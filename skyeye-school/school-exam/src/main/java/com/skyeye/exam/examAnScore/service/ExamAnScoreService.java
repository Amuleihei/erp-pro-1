package com.skyeye.exam.examAnScore.service;

import com.skyeye.base.business.service.SkyeyeBusinessService;
import com.skyeye.common.object.InputObject;
import com.skyeye.common.object.OutputObject;
import com.skyeye.exam.examAnScore.entity.ExamAnScore;

public interface ExamAnScoreService extends SkyeyeBusinessService<ExamAnScore> {

    void queryExamAnScoreListById(InputObject inputObject, OutputObject outputObject);
}
