/*
 * This file is part of UNN-Android.
 *
 * UNN-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UNN-Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with UNN-Android.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.kmichaelk.unnandroid.network

import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.decodeCertificatePem

private val globalSignUnnCertificationAuthority =
        """
    -----BEGIN CERTIFICATE-----
    MIIHTDCCBjSgAwIBAgIMB86ly3DEt6WQX8/7MA0GCSqGSIb3DQEBCwUAMFUxCzAJ
    BgNVBAYTAkJFMRkwFwYDVQQKExBHbG9iYWxTaWduIG52LXNhMSswKQYDVQQDEyJH
    bG9iYWxTaWduIEdDQyBSNiBBbHBoYVNTTCBDQSAyMDIzMB4XDTI0MDYwMzEzNDUy
    MFoXDTI1MDcwNTEzNDUxOVowEzERMA8GA1UEAwwIKi51bm4ucnUwggIiMA0GCSqG
    SIb3DQEBAQUAA4ICDwAwggIKAoICAQC8ZBFnp9rBTZMzoSXTUIbbixHTqfJuo4qa
    91o2CA/c5Q8i8jhPxzxp8SHaTAPUW1zaEpK7JYlkYWxCdDXwEYouwNgXI2UubIpF
    vXtD5+SCRuTtqMon1yzG6Y824WxfDiyjRT4xTf9stChy9zQDD2jRTuLV/5rAqzV7
    srjOA25FTuaRIVzE/vNUHqNn7iVkwiUGjEHd4lEIzV/0XWUP4/fpHTpjhLA0gWVR
    1GUmjzzIc2UG8pf9+1CjGDPPl1Kx+MnXNACrr3lLsznq1yIml/CID3Yp90M7wa6C
    gQy3OVGzZ7a6R4DI1Hkvb6KS/PIMmF9fhW9+qakaIYW5PUQn/8c6h41S+6euUppI
    YIPmNxqI4PXIxEY5Y3QHNY5y0DUm9k5EaDBrYIWawN+CKIKFrkAPi2W/mmqLb63p
    AtCqWVqOBCE/bYXb8zzHwLGESMZJSOC3qqJW43uE7TIP/+RbDVr1igOhik9fX/aN
    /3p+8bMhFxofNi3KYhaFA2h10n8UdyawKi4EvoHXyqyCL4u9Phrvl7on31xDhG38
    GeejFUVSTr8bfqQu4/lOiEd66oUJcIl4ZMHxYIJMyh9a8YklAr17df0+olAFd+NV
    GX5H+QMbyWT7rvjy7n6N7SYG0NMrXSWn2FUbRxGeboBNE1IPPVJ35xn5jN1DqY+Q
    tEynB7+UcQIDAQABo4IDXDCCA1gwDgYDVR0PAQH/BAQDAgWgMAwGA1UdEwEB/wQC
    MAAwgZkGCCsGAQUFBwEBBIGMMIGJMEkGCCsGAQUFBzAChj1odHRwOi8vc2VjdXJl
    Lmdsb2JhbHNpZ24uY29tL2NhY2VydC9nc2djY3I2YWxwaGFzc2xjYTIwMjMuY3J0
    MDwGCCsGAQUFBzABhjBodHRwOi8vb2NzcC5nbG9iYWxzaWduLmNvbS9nc2djY3I2
    YWxwaGFzc2xjYTIwMjMwVwYDVR0gBFAwTjAIBgZngQwBAgEwQgYKKwYBBAGgMgoB
    AzA0MDIGCCsGAQUFBwIBFiZodHRwczovL3d3dy5nbG9iYWxzaWduLmNvbS9yZXBv
    c2l0b3J5LzBEBgNVHR8EPTA7MDmgN6A1hjNodHRwOi8vY3JsLmdsb2JhbHNpZ24u
    Y29tL2dzZ2NjcjZhbHBoYXNzbGNhMjAyMy5jcmwwGwYDVR0RBBQwEoIIKi51bm4u
    cnWCBnVubi5ydTAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwHwYDVR0j
    BBgwFoAUvQW384qTPHPLefoPhRKhd5YYkXQwHQYDVR0OBBYEFM6KU/sq7qqRmabt
    TlPHWQISpPduMIIBfwYKKwYBBAHWeQIEAgSCAW8EggFrAWkAdwAaBP9J0FQdQK/2
    oMO/8djEZy9O7O4jQGiYaxdALtyJfQAAAY/eWPOOAAAEAwBIMEYCIQC1ATHq+wzz
    xDQvSQ6vXIRno9hpyU8QO3O8bs3PPKzQzwIhAMI2+LdWjU5HX2oE23cEDVkz+Jy3
    A9q/Zd944r89GDuyAHUADeHyMCvTDcFAYhIJ6lUu/Ed0fLHX6TDvDkIetH5OqjQA
    AAGP3ljzbgAABAMARjBEAiBULkEztfLAJ37uktCIyDhL0+egUsKI1lVuf0zOpwsB
    QwIgegLOEF3A5Vgm7me+g79ilvQbbi90N6H4v2BKgKawmw4AdwDd3Mo0ldfhFgXn
    lTL6x5/4PRxQ39sAOhQSdgosrLvIKgAAAY/eWPOKAAAEAwBIMEYCIQD1nfUT9hAh
    +J+Wi6U5S7SaPExHu5iNwQAQsH1kPxr/UAIhAPx+P1Gf+DiLsYLk7ojW7sld7lz0
    dDZOghnyaIKP7EZ9MA0GCSqGSIb3DQEBCwUAA4IBAQAv2ZDXxEaRP19JH+rClMyk
    uu0TbgRtAv3CJtmEXFEdElg2hO/oDe5SFDI+IkReIaEbP1nuqhedblDL2NKMQkAe
    w4+ELLkUktQ/UJNDmZhacNPie/ntlpO3x8y6qiUmEu7tA9WxAr0SQadeER9lILTR
    lK+lnk3335Yo6lJkeXxZTLPd1k6/ED2/GFCzHId8RZIRbFmAVpk8icMzfPP0w+aC
    EcyrD1jbVCCyNLoXGFO/hGjXMiKIL8UYXoFayzQF5sIK3ebmAxJ0cZGIYHXx4cwi
    L+zT15de2FV23f0GFQ/HGMiLQ68TCJVUNhdfp9DkJMPr2uM+tPBLd5Zap+quJ6Pa
    NVOFBkpdn627G190
    -----END CERTIFICATE-----
    """.trimIndent().decodeCertificatePem()

fun buildUnnCertificates() = HandshakeCertificates.Builder()
    .addTrustedCertificate(globalSignUnnCertificationAuthority)
    .addPlatformTrustedCertificates()
    .build()
