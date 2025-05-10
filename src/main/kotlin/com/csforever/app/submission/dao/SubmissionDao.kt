package com.csforever.app.submission.dao

import com.csforever.app.submission.model.Submission

interface SubmissionDao {

    suspend fun insert(submission: Submission): Submission
}