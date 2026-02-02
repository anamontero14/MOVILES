package com.example.elhostal.data.converter

import androidx.room.TypeConverter
import com.example.elhostal.domain.roles.UserRole

class UserRoleConverter {
    @TypeConverter
    fun fromUserRole(role: UserRole): String {
        return role.name
    }

    @TypeConverter
    fun toUserRole(roleName: String): UserRole {
        return UserRole.valueOf(roleName)
    }
}