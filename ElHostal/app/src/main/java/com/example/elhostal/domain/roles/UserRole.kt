package com.example.elhostal.domain.roles

enum class UserRole(val permissions: Set<AppPermission>) {
    GUEST(
        setOf(
            AppPermission.VIEW_ROOMS  // Solo puede ver
        )
    ),
    USER(
        setOf(
            AppPermission.VIEW_ROOMS,
            AppPermission.MAKE_RESERVATION  // Puede ver y reservar
        )
    ),
    ADMIN(
        setOf(
            AppPermission.VIEW_ROOMS,
            AppPermission.CREATE_ROOM,
            AppPermission.EDIT_ROOM,
            AppPermission.DELETE_ROOM,
            AppPermission.MAKE_RESERVATION,
            AppPermission.MANAGE_USERS  // Puede hacer todo
        )
    )
}