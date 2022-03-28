package com.infineon.tpm20.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.security.cert.X509Certificate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Overlay the default application properties with properties from test
@ActiveProfiles("test")
public class CAServiceTests {

    private final String slb9670EkCrt_P256Sha256 = "MIIDEDCCAragAwIBAgIEWJh/4TAKBggqhkjOPQQDAjCBgzELMAkGA1UEBhMCREUxITAfBgNVBAoMGEluZmluZW9uIFRlY2hub2xvZ2llcyBBRzEaMBgGA1UECwwRT1BUSUdBKFRNKSBUUE0yLjAxNTAzBgNVBAMMLEluZmluZW9uIE9QVElHQShUTSkgRUNDIE1hbnVmYWN0dXJpbmcgQ0EgMDIwMB4XDTE3MDIyMzA0NDE0MloXDTMyMDIyMzA0NDE0MlowADBZMBMGByqGSM49AgEGCCqGSM49AwEHA0IABPEe6WkVH+1fJTATBJgH9MHuPddGDO5H+/kLxJ979E/uDUQj8qm0e10JJMhVjG6ghuoCyJiciFkp7WnbJ0UOeQijggGYMIIBlDBbBggrBgEFBQcBAQRPME0wSwYIKwYBBQUHMAKGP2h0dHA6Ly9wa2kuaW5maW5lb24uY29tL09wdGlnYUVjY01mckNBMDIwL09wdGlnYUVjY01mckNBMDIwLmNydDAOBgNVHQ8BAf8EBAMCAAgwWAYDVR0RAQH/BE4wTKRKMEgxFjAUBgVngQUCAQwLaWQ6NDk0NjU4MDAxGjAYBgVngQUCAgwPU0xCIDk2NzAgVFBNMi4wMRIwEAYFZ4EFAgMMB2lkOjA3M0QwDAYDVR0TAQH/BAIwADBQBgNVHR8ESTBHMEWgQ6BBhj9odHRwOi8vcGtpLmluZmluZW9uLmNvbS9PcHRpZ2FFY2NNZnJDQTAyMC9PcHRpZ2FFY2NNZnJDQTAyMC5jcmwwFQYDVR0gBA4wDDAKBggqghQARAEUATAfBgNVHSMEGDAWgBQKjBd2ZnzumIV2jaxAHgILmFdhYzAQBgNVHSUECTAHBgVngQUIATAhBgNVHQkEGjAYMBYGBWeBBQIQMQ0wCwwDMi4wAgEAAgF0MAoGCCqGSM49BAMCA0gAMEUCICo/CZHM1c5TFndh42QmEej08O33K1cW4rHyG9ymL391AiEAhJkLYM02FEuaAjPuCyFAOf0r0WbxGFWkcDbx6Npb5c4=";
    private final String slb9670EkCrt_Rsa2048Sha256 = "MIIEnDCCA4SgAwIBAgIEW+8QZDANBgkqhkiG9w0BAQsFADCBgzELMAkGA1UEBhMCREUxITAfBgNVBAoMGEluZmluZW9uIFRlY2hub2xvZ2llcyBBRzEaMBgGA1UECwwRT1BUSUdBKFRNKSBUUE0yLjAxNTAzBgNVBAMMLEluZmluZW9uIE9QVElHQShUTSkgUlNBIE1hbnVmYWN0dXJpbmcgQ0EgMDIwMB4XDTE3MDIyMzA0NDE1OVoXDTMyMDIyMzA0NDE1OVowADCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJC378sgW3ttNNNRtDLz+iye5yba8nOcMGvHPVFZFDbJHU3+aP80DPOh0ZPgT8huwfCl+rPU1fbPcKUApBsnotuxs9tmKFGTUnK8Ko6TPxxFbx28l2IDhw5bQAUKcXNX52hXS1AtVhUR1MphR6I2gIuJYaUpiHc0ebMfIdpBWQg2JwQc3Wm++SfUDFpCDPjst1svswItW2Oo5qeOd2eI3J+Jst2K8XZZKNAypYOnUGbw9umrUfjmEz8/XWgK7vZLy2DGSTTSTg+8KPoJ295c75E5tlLz67L9f2pQ0clF69o4khfz7MJkzlqvpVyjGLF7766kSe877Zx5PjD6IeEAT1cCAwEAAaOCAZgwggGUMFsGCCsGAQUFBwEBBE8wTTBLBggrBgEFBQcwAoY/aHR0cDovL3BraS5pbmZpbmVvbi5jb20vT3B0aWdhUnNhTWZyQ0EwMjAvT3B0aWdhUnNhTWZyQ0EwMjAuY3J0MA4GA1UdDwEB/wQEAwIAIDBYBgNVHREBAf8ETjBMpEowSDEWMBQGBWeBBQIBDAtpZDo0OTQ2NTgwMDEaMBgGBWeBBQICDA9TTEIgOTY3MCBUUE0yLjAxEjAQBgVngQUCAwwHaWQ6MDczRDAMBgNVHRMBAf8EAjAAMFAGA1UdHwRJMEcwRaBDoEGGP2h0dHA6Ly9wa2kuaW5maW5lb24uY29tL09wdGlnYVJzYU1mckNBMDIwL09wdGlnYVJzYU1mckNBMDIwLmNybDAVBgNVHSAEDjAMMAoGCCqCFABEARQBMB8GA1UdIwQYMBaAFDLG5XZmP+6A1kzdexjlYDhjs7yKMBAGA1UdJQQJMAcGBWeBBQgBMCEGA1UdCQQaMBgwFgYFZ4EFAhAxDTALDAMyLjACAQACAXQwDQYJKoZIhvcNAQELBQADggEBAFNFiq8VSjojGl9kJBvqrCsUVKWOK0pfNFiITgWhOogKJzlRDwJjT9jjLwRgDpfiza0PST3dTTEdU79ucIWzMcEvzuXve8wFX6FW0y3ubJfSNntZ79MBlCiL/QtUQs+nI4EqyB3EEZeZQ/XR6qc5MktGv4HKgCsrLgC/K5WDtX8ksaIptN4x+j2aif2HG5EpxPEQgxH+LK4b2f7phsDm1CJveK2LSbnFjcWbe/TzvjYxVKJap7Wdbizd94gP9c0yluoc9Hn3ifcFBk/iB2Vx65dGsxcyaGyyKz5C2E+ZP9YU2tZTS8vax5J5SY96/JsGAmap9RmmvVw/rsB5JPVfOQY=";
    private final String slb9672EkCrt_P256Sha512 = "MIIDSjCCAqugAwIBAgIEUj0DRTAKBggqhkjOPQQDBDB2MQswCQYDVQQGEwJERTEhMB8GA1UECgwYSW5maW5lb24gVGVjaG5vbG9naWVzIEFHMRMwEQYDVQQLDApPUFRJR0EoVE0pMS8wLQYDVQQDDCZJbmZpbmVvbiBPUFRJR0EoVE0pIFRQTSAyLjAgRUNDIENBIDA1NjAeFw0yMTA3MDQxNjM4MzJaFw0zNjA3MDQxNjM4MzJaMAAwWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAARtGC2iV2hI2P81Xfr8mxC/v4FGMCeCSYJw3N+Ke7OPpbCE+7hCAbwCUk9hskwHpl5ex4+8ndZPU+6/ciymH7Zzo4IBmzCCAZcwXAYIKwYBBQUHAQEEUDBOMEwGCCsGAQUFBzAChkBodHRwczovL3BraS5pbmZpbmVvbi5jb20vT3B0aWdhRWNjTWZyQ0EwNTYvT3B0aWdhRWNjTWZyQ0EwNTYuY3J0MA4GA1UdDwEB/wQEAwIACDBYBgNVHREBAf8ETjBMpEowSDEWMBQGBWeBBQIBDAtpZDo0OTQ2NTgwMDEaMBgGBWeBBQICDA9TTEIgOTY3MiBUUE0yLjAxEjAQBgVngQUCAwwHaWQ6MEYxNTAMBgNVHRMBAf8EAjAAMFEGA1UdHwRKMEgwRqBEoEKGQGh0dHBzOi8vcGtpLmluZmluZW9uLmNvbS9PcHRpZ2FFY2NNZnJDQTA1Ni9PcHRpZ2FFY2NNZnJDQTA1Ni5jcmwwFQYDVR0gBA4wDDAKBggqghQARAEUATAfBgNVHSMEGDAWgBQoL1ZwsR6IsopJWbJs//rs3Pl86DAQBgNVHSUECTAHBgVngQUIATAiBgNVHQkEGzAZMBcGBWeBBQIQMQ4wDAwDMi4wAgEAAgIAnzAKBggqhkjOPQQDBAOBjAAwgYgCQgEK/siUV8xZcpO6U4WPwXcBvkcoGTtL6IvKRLcb5Sk/OlwXJGZ31xan8mvRAFQPfE8gsVJv6X5/u13NBVGY6sceHAJCAIEXmm2LeDRlIklaeECNVtfTBJkMFbgIzqPXN0zSgbiot+w4WnpovocwtPEBvfjwGwm0m2FGJXOzw037DYjVbF1b";
    private final String slb9672EkCrt_P384Sha512 = "MIIDZzCCAsigAwIBAgIEUC1DDjAKBggqhkjOPQQDBDB2MQswCQYDVQQGEwJERTEhMB8GA1UECgwYSW5maW5lb24gVGVjaG5vbG9naWVzIEFHMRMwEQYDVQQLDApPUFRJR0EoVE0pMS8wLQYDVQQDDCZJbmZpbmVvbiBPUFRJR0EoVE0pIFRQTSAyLjAgRUNDIENBIDA1NjAeFw0yMTA3MDQxNjM4NTVaFw0zNjA3MDQxNjM4NTVaMAAwdjAQBgcqhkjOPQIBBgUrgQQAIgNiAASNuVtJfSGEU354XD/RjKKg2SSzTg8QbtMQWMBKnjWbXSE5XOvDTab2shNcBPHUYwFtQm+ICXDNJTJZ1l/E9n5nvf9mW/GGSUn2hYhhLHmqKm1dc0KBSfrPwzIWNYOhH/ijggGbMIIBlzBcBggrBgEFBQcBAQRQME4wTAYIKwYBBQUHMAKGQGh0dHBzOi8vcGtpLmluZmluZW9uLmNvbS9PcHRpZ2FFY2NNZnJDQTA1Ni9PcHRpZ2FFY2NNZnJDQTA1Ni5jcnQwDgYDVR0PAQH/BAQDAgAIMFgGA1UdEQEB/wROMEykSjBIMRYwFAYFZ4EFAgEMC2lkOjQ5NDY1ODAwMRowGAYFZ4EFAgIMD1NMQiA5NjcyIFRQTTIuMDESMBAGBWeBBQIDDAdpZDowRjE1MAwGA1UdEwEB/wQCMAAwUQYDVR0fBEowSDBGoESgQoZAaHR0cHM6Ly9wa2kuaW5maW5lb24uY29tL09wdGlnYUVjY01mckNBMDU2L09wdGlnYUVjY01mckNBMDU2LmNybDAVBgNVHSAEDjAMMAoGCCqCFABEARQBMB8GA1UdIwQYMBaAFCgvVnCxHoiyiklZsmz/+uzc+XzoMBAGA1UdJQQJMAcGBWeBBQgBMCIGA1UdCQQbMBkwFwYFZ4EFAhAxDjAMDAMyLjACAQACAgCfMAoGCCqGSM49BAMEA4GMADCBiAJCAWDMLIkEedqrEY5jZPH+itE23ESM+Sam6n09QuRL0M0b2sM19ycKFZCFdsJmQWLXGk+K7aMMx3YPqLeXcLT8I2LLAkIBrJ1AT7RROBTSJpCP64a6xbATKAIbq1MKb3Y3SJnTsoE2gB0U/tKikX4EHGieONaaIxJESRkm2FIfcgQl25daftg=";
    private final String slb9672EkCrt_Rsa2048Sha384 = "MIIFkTCCA3mgAwIBAgIEaS7oXTANBgkqhkiG9w0BAQwFADB2MQswCQYDVQQGEwJERTEhMB8GA1UECgwYSW5maW5lb24gVGVjaG5vbG9naWVzIEFHMRMwEQYDVQQLDApPUFRJR0EoVE0pMS8wLQYDVQQDDCZJbmZpbmVvbiBPUFRJR0EoVE0pIFRQTSAyLjAgUlNBIENBIDA1NjAeFw0yMTA3MDQxNjM5NDFaFw0zNjA3MDQxNjM5NDFaMAAwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCa/B7naj8Yxor4NSJvGxCW9vuSvqFs+KTIYfNE5vPYDfV0CTUa3jjDBAqdYLZ3SmfXzSLrWgjiilt/lxHXfX6uwsZL/YTFObnjYlW5qY0+akVQXYIYXcutXiC01O0NzeFmDbCzQcIhzNUQUu1FGKPfAOkCDoDEWgw2c+Nvz7t0+TrHgjpRsS1VM6HnuMxkxsjMg5GN6KzVmtRK2Iomnz8nlOG2ec+HaRjPekcIMbIz8xc34S8C0bE2uggZgoOyIT8/zw3YvMBfxk5SkxlxLpUxak1lco69ZzlNqLjaRFJDWeR+qJdWuvT+HIL9TvIemgTgrdmqtCqMFOioyQd4jyYbAgMBAAGjggGbMIIBlzBcBggrBgEFBQcBAQRQME4wTAYIKwYBBQUHMAKGQGh0dHBzOi8vcGtpLmluZmluZW9uLmNvbS9PcHRpZ2FSc2FNZnJDQTA1Ni9PcHRpZ2FSc2FNZnJDQTA1Ni5jcnQwDgYDVR0PAQH/BAQDAgAgMFgGA1UdEQEB/wROMEykSjBIMRYwFAYFZ4EFAgEMC2lkOjQ5NDY1ODAwMRowGAYFZ4EFAgIMD1NMQiA5NjcyIFRQTTIuMDESMBAGBWeBBQIDDAdpZDowRjE1MAwGA1UdEwEB/wQCMAAwUQYDVR0fBEowSDBGoESgQoZAaHR0cHM6Ly9wa2kuaW5maW5lb24uY29tL09wdGlnYVJzYU1mckNBMDU2L09wdGlnYVJzYU1mckNBMDU2LmNybDAVBgNVHSAEDjAMMAoGCCqCFABEARQBMB8GA1UdIwQYMBaAFNbBjwVwlLNdvxmD6Hf16BM4fRP6MBAGA1UdJQQJMAcGBWeBBQgBMCIGA1UdCQQbMBkwFwYFZ4EFAhAxDjAMDAMyLjACAQACAgCfMA0GCSqGSIb3DQEBDAUAA4ICAQBLnLTi3x548wuKEz8miOIuKGqAFSCN2Rfv6GLZ4agHxCZD8EZtrfH+L6zdXAH/Sv7/SciYIsWlkeJr826ERuQXCVv3bARWaI6Hd6R4luQbTltlnYQXRika8S36v+NiP7X/AdiFflVnIQ03EYXMpfK93Scv8mOGtW30aZy3Ce2WBWVKbdNdgwP363DABnkbT7T9KeyQEvillpqadyuJAPiXGlwebzMfmsjC+KSYWBf5c7xW5l+Kc6i6eHiylAB/9xy4Bv4avtkCzSw4bKilLVa7phjgAYhNd6NZ0bSnAs939wOxBj4YxvOOlyuPom24iN9RMhssf13XHP8bYIC3IhzGxZeQ5ULyBH3N8NSAkkg4O/lhhHPFbXgAjdeH+lN8WYxb9nv5j66YTnlfMIE9c8gTdP3IFaBRwvv5CW9kC4WisX2xi4ef6JsJiU3iZbpTUjgoIk8eTFg6pXQ+MNmVrJU8IZwuDfUyCC8Y/VOgvTkwCnilgJOHPS/Ae6rNtyGXGwip7pOc+vlkoKcTUfyd2rkfNeaS8Bqc++wXfrw10D99NWdvgfdUi3WXe8VtpVa66OwwGUuoOD6wHSzrffA+2TnvfbX+LjHZTD6T/GFtBKCkLxG/NnX9BB/bm+Y4lmqbOXLdub+W8/2aAJASdvm1RNfQdm5Xjjfq8JsmvU5Z42if6g==";

    @Autowired
    private CAService caService;

    @Test
    void verify() {
        Assertions.assertDoesNotThrow(() -> {
            X509Certificate x509Certificate = caService.base64(slb9670EkCrt_P256Sha256);
            Assertions.assertNotNull(x509Certificate);
            Assertions.assertEquals(x509Certificate.getIssuerX500Principal().getName(),
                    "CN=Infineon OPTIGA(TM) ECC Manufacturing CA 020,OU=OPTIGA(TM) TPM2.0,O=Infineon Technologies AG,C=DE");
            caService.verify(x509Certificate);

            x509Certificate = caService.base64(slb9670EkCrt_Rsa2048Sha256);
            Assertions.assertNotNull(x509Certificate);
            Assertions.assertEquals(x509Certificate.getIssuerX500Principal().getName(),
                    "CN=Infineon OPTIGA(TM) RSA Manufacturing CA 020,OU=OPTIGA(TM) TPM2.0,O=Infineon Technologies AG,C=DE");
            caService.verify(x509Certificate);

            x509Certificate = caService.base64(slb9672EkCrt_P256Sha512);
            Assertions.assertNotNull(x509Certificate);
            Assertions.assertEquals(x509Certificate.getIssuerX500Principal().getName(),
                    "CN=Infineon OPTIGA(TM) TPM 2.0 ECC CA 056,OU=OPTIGA(TM),O=Infineon Technologies AG,C=DE");
            caService.verify(x509Certificate);

            x509Certificate = caService.base64(slb9672EkCrt_P384Sha512);
            Assertions.assertNotNull(x509Certificate);
            Assertions.assertEquals(x509Certificate.getIssuerX500Principal().getName(),
                    "CN=Infineon OPTIGA(TM) TPM 2.0 ECC CA 056,OU=OPTIGA(TM),O=Infineon Technologies AG,C=DE");
            caService.verify(x509Certificate);

            x509Certificate = caService.base64(slb9672EkCrt_Rsa2048Sha384);
            Assertions.assertNotNull(x509Certificate);
            Assertions.assertEquals(x509Certificate.getIssuerX500Principal().getName(),
                    "CN=Infineon OPTIGA(TM) TPM 2.0 RSA CA 056,OU=OPTIGA(TM),O=Infineon Technologies AG,C=DE");
            caService.verify(x509Certificate);
        });
    }
}
