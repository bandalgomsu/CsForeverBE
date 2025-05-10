package com.csforever.app.submission.implement

import com.csforever.app.submission.dao.SubmissionDao
import org.springframework.stereotype.Component

@Component
class SubmissionFinder(
    private val submissionDao: SubmissionDao
) {


}