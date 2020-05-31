# Security tools

This modules provides tools for encrypt/decrypt things with maximum security.

The issue with encrypting/decrypting is to store the private key.
Here we use Android Keystore system to have a key associated to the application.
Only the application can have the key, so nobody else can have the key,
except hack Android system.

To use it, use the [Security](../main/java/fr/jhelp/security/Security.kt)
object or tools provides in [Crypt](../main/java/fr/jhelp/security/Crypt.kt).

In both, `encrypt` methods encrypt things and `decrypt` methods decrypt it.

