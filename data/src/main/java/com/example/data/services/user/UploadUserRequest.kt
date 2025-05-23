package com.example.data.services.user

import com.example.data.models.User

data class UploadUserRequest(
    val name: String? = null,
    val imageId: Long? = null
) {
    constructor(user: User) : this(
        name = user.name,
        imageId = user.image?.id
    )
}