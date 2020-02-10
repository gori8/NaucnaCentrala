keytool -genkeypair -keyalg RSA -keysize 2048 -alias front -dname "CN=KOBE_FOREVERRRRRRRR,OU=Development,O=FRONTNC,C=NL" -ext "SAN:c=DNS:192.168.43.86,IP:192.168.43.86" -validity 3650 -keystore identityOnPN.jks -storepass secret -keypass secret -deststoretype pkcs12

#izvuci frontnc.cer iz identity.jks od frontnc
keytool -exportcert -keystore identityOnPN.jks -storepass secret -alias front -rfc -file frontOnPN.cer

#ubaci u truststore od nc cert od frontnc
keytool -keystore /home/antolovic/KP_SEP/KP/naucna-centrala/webshop/src/main/resources/truststore.jks -importcert -file frontOnPN.cer -alias frontonpn -storepass secret

#izvlacenje .p12 iz identity.jks od nc
keytool -importkeystore \
    -srckeystore identityOnPN.jks \
    -destkeystore keystoreOnPN.p12 \
    -deststoretype PKCS12 \
    -srcalias front \
    -deststorepass secret \
    -destkeypass secret

#izvlacenje nc.crt
openssl pkcs12 -in keystoreOnPN.p12 -nokeys -out frontOnPN.crt

#izvlacenje nc.key
openssl pkcs12 -in keystoreOnPN.p12 -nocerts -nodes -out frontOnPN.key

#importuj sertifikat u browser
pk12util -d sql:$HOME/.pki/nssdb -i /home/antolovic/KP_SEP/KP/ssl/keystoreOnPN.p12
