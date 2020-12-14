package com.example.doctor_app.model

import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.util.*
import javax.security.auth.x500.X500Principal
import kotlin.collections.HashMap

data class User(val name: String, val mail: String, val success: Boolean)