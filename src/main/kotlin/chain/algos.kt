package chain

import java.security.MessageDigest

fun sha256(s: String): String {
    //convert to hex
    val hex = s.toByteArray(Charsets.UTF_8)
    //convert to byte array
    val bytes = ByteArray(hex.size / 2)
    for (i in 0 until bytes.size) {
        val b = hex[i * 2].toInt() - 48
        bytes[i] = ((b shl 4) + (hex[i * 2 + 1].toInt() - 48)).toByte()
    }
    //hash
    val digest = MessageDigest.getInstance("SHA-256")
    digest.update(bytes)
    return digest.digest().toHexString()
}
fun sha512(s: String): String {
    //convert to hex
    val hex = s.toByteArray(Charsets.UTF_8)
    //convert to byte array
    val bytes = ByteArray(hex.size / 2)
    for (i in 0 until bytes.size) {
        val b = hex[i * 2].toInt() - 48
        bytes[i] = ((b shl 4) + (hex[i * 2 + 1].toInt() - 48)).toByte()
    }
    //hash
    val digest = MessageDigest.getInstance("SHA-512")
    digest.update(bytes)
    return digest.digest().toHexString()
}

fun sha256(s: Long): String {
    return sha256(s.toString())
}
fun sha512(s: Long): String {
    return sha512(s.toString())
}
private fun ByteArray.toHexString(): String {
    val sb = StringBuilder()
    for (b in this) {
        sb.append(String.format("%02x", b))
    }
    return sb.toString()
}
