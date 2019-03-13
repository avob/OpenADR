###################################################
# This script setup a custom PKI architecture containing:
# - custom certificate authority
# - server vtn rsa/ecc
# - client ven1 rsa
# - client ven2 ecc
# - client admin rsa
# use openssl_client / curl to test ssl handshake between components:
# openssl s_client -key ven1.oadr.com.key -cert ven1.oadr.com.crt -CAfile oadr.com.crt -connect vtn.oadr.com:8181
# curl --key admin.oadr.com.key --cert admin.oadr.com.crt --cacert oadr.com.crt  \
#	-d '{"username": "35:C2:79:32:5B:62:65:A7:AA:D3}' -H "Content-Type: application/json" -X POST https://localhost:8181/testvtn/Ven/ 
###################################################

CA_NAME="oadr.com"
CLIENT_NAME="admin.oadr.com"
VTN_NAME="vtn.oadr.com"
VEN1_NAME="ven1.oadr.com"
VEN2_NAME="ven2.oadr.com"

###################################
# CUSTOM CERTIFICATION AUTHORITY
###################################
# Creating CA Certificate
# create ca.key /ca.crt
openssl req -nodes -new -x509  -keyout $CA_NAME.key -out $CA_NAME.crt -subj "/C=FR/ST=Paris/L=Paris/O=Avob/OU=Avob/CN=$CA_NAME"

###################################
# CLIENT RSA
###################################
# Creating a Key and CSR for the Server
# create $CLIENT_NAME.key / $CLIENT_NAME.csr
openssl req -newkey rsa:2048 -nodes -keyout $CLIENT_NAME.key -out $CLIENT_NAME.csr -subj "/C=FR/ST=Paris/L=Paris/O=Avob/OU=Avob/CN=$CLIENT_NAME"

# Signing Server Certificate with previously created CA.
# $CLIENT_NAME.crt = $CLIENT_NAME.csr + $CA_NAME.crt + $CA_NAME.key
openssl x509 -req -days 365 -in $CLIENT_NAME.csr -CA $CA_NAME.crt -CAkey $CA_NAME.key -set_serial 01 -out $CLIENT_NAME.crt

# Compute client fingerprint
openssl x509 -in $CLIENT_NAME.crt -fingerprint -sha256 -noout | cut -d'=' -f2 | cat | tail -c 30 > $CLIENT_NAME.fingerprint

# Creating PKCS12 for the Client
# $CLIENT_NAME.p12 = $CLIENT_NAME.key + $CLIENT_NAME.crt + $CA_NAME.crt
openssl pkcs12 -export  -inkey $CLIENT_NAME.key -in $CLIENT_NAME.crt -certfile $CA_NAME.crt -out $CLIENT_NAME.p12

###################################
# VTN RSA
###################################
# Creating a Key and CSR for the Server
# create $VTN_NAME-rsa..key / $VTN_NAME-rsa..csr
openssl req -newkey rsa:2048 -nodes -keyout $VTN_NAME-rsa.key -out $VTN_NAME-rsa.csr -subj "/C=FR/ST=Paris/L=Paris/O=Avob/OU=Avob/CN=$VTN_NAME"

# Signing Server Certificate with previously created CA.
# $VTN_NAME-rsa. = $VTN_NAME-rsa..csr + $CA_NAME.crt + $CA_NAME.key
openssl x509 -req -days 365 -in $VTN_NAME-rsa.csr -CA $CA_NAME.crt -CAkey $CA_NAME.key -set_serial 02 -out $VTN_NAME-rsa.crt

###################################
# VTN ECC 256-bit
###################################
# Creating a Key and CSR for the Server
# create $VTN_NAME.key / $VTN_NAME.csr
openssl req -new -x509 -nodes -newkey ec:<(openssl ecparam -name secp384r1) -keyout $VTN_NAME.key -subj "/C=FR/ST=Paris/L=Paris/O=Avob/OU=Avob/CN=$VTN_NAME"
openssl req -new -key $VTN_NAME.key -out $VTN_NAME.csr -sha256 -subj "/C=FR/ST=Paris/L=Paris/O=Avob/OU=Avob/CN=$VTN_NAME"

# Signing Server Certificate with previously created CA.
# $VTN_NAME.crt = $VTN_NAME.csr + $CA_NAME.crt + $CA_NAME.key
openssl x509 -req -days 365 -in $VTN_NAME.csr -CA $CA_NAME.crt -CAkey $CA_NAME.key -set_serial 03 -out $VTN_NAME.crt

# Compute VEN fingerprint
openssl x509 -in $VTN_NAME.crt -fingerprint -sha256 -noout | cut -d'=' -f2 | cat | tail -c 30 > $VTN_NAME.fingerprint

###################################
# VEN1 RSA
###################################
# Creating a Key and CSR for the Server
# create $VEN1_NAME.key / $VEN1_NAME.csr
openssl req -newkey rsa:2048 -nodes -keyout $VEN1_NAME.key -out $VEN1_NAME.csr -subj "/C=FR/ST=Paris/L=Paris/O=Avob/OU=Avob/CN=$VEN1_NAME"

# Signing Server Certificate with previously created CA.
# $VEN1_NAME.crt = $VEN1_NAME.csr + $CA_NAME.crt + $CA_NAME.key
openssl x509 -req -days 365 -in $VEN1_NAME.csr -CA $CA_NAME.crt -CAkey $CA_NAME.key -set_serial 04 -out $VEN1_NAME.crt

# Compute VEN fingerprint
openssl x509 -in $VEN1_NAME.crt -fingerprint -sha256 -noout | cut -d'=' -f2 | cat | tail -c 30 > $VEN1_NAME.fingerprint

###################################
# VEN2 ECC 256-bit
###################################
# Creating a Key and CSR for the Server
# create $VEN2_NAME.key / $VEN2_NAME.csr
openssl req -new -x509 -nodes -newkey ec:<(openssl ecparam -name secp384r1) -keyout $VEN2_NAME.key -subj "/C=FR/ST=Paris/L=Paris/O=Avob/OU=Avob/CN=$VEN2_NAME"
openssl req -new -key $VEN2_NAME.key -out $VEN2_NAME.csr -sha256 -subj "/C=FR/ST=Paris/L=Paris/O=Avob/OU=Avob/CN=$VEN2_NAME"

# Signing Server Certificate with previously created CA.
# $VEN2_NAME.crt = $VEN2_NAME.csr + $CA_NAME.crt + $CA_NAME.key
openssl x509 -req -days 365 -in $VEN2_NAME.csr -CA $CA_NAME.crt -CAkey $CA_NAME.key -set_serial 05 -out $VEN2_NAME.crt

# Compute VEN fingerprint
openssl x509 -in $VEN2_NAME.crt -fingerprint -sha256 -noout | cut -d'=' -f2 | cat | tail -c 30 > $VEN2_NAME.fingerprint


