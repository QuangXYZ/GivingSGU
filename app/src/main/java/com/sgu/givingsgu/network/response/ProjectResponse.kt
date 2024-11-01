package com.sgu.givingsgu.network.response

import com.sgu.givingsgu.model.Project

class ProjectResponse(
    val project: Project,
    val likeCount: Int,
    val liked: Boolean
) {

}