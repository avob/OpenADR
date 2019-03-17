###################################################
# This script setup a custom PKI architecture containing:
# - custom certificate authority
# - server vtn rsa/ecc
# - client admin rsa
# - client ven1 rsa
# - client ven2 ecc
# use openssl_client / curl to test ssl handshake between components:
# openssl s_client -key ven1.oadr.com.key -cert ven1.oadr.com.crt -CAfile oadr.com.crt -connect vtn.oadr.com:8181
# curl --key admin.oadr.com.key --cert admin.oadr.com.crt --cacert oadr.com.crt  \
#	-d '{"username": "35:C2:79:32:5B:62:65:A7:AA:D3}' -H "Content-Type: application/json" -X POST https://localhost:8181/testvtn/Ven/ 
###################################################
COUNTRY="FR"
STATE="Paris"
LOCALITY="Paris"
ORGANIZATION="Avob"	

CA_NAME="oadr.com"
CLIENT_NAME="admin.oadr.com"
VTN_NAME="vtn.oadr.com"
VEN1_NAME="ven1.oadr.com"
VEN2_NAME="ven2.oadr.com"

###################################
# HELPERS
###################################
# write_openssl_config_file "out file name" "certificate CN" "certificate default_bits"
write_openssl_config_file()
{
	FILENAME=$1
	cat > $FILENAME <<- EOM
	[ req ]
	default_bits       = $3
	distinguished_name = req_distinguished_name
	req_extensions     = req_ext
	[ req_distinguished_name ]
	countryName                 = $COUNTRY
	stateOrProvinceName         = $STATE
	localityName               = $LOCALITY
	organizationName           = $ORGANIZATION
	commonName                 = $2
	[ req_ext ]
	subjectAltName = @alt_names
	[alt_names]
	DNS.1   = localhost
	EOM
}
# gen_rsa_certificate "key/csr/crt files name" "certificate CN" "in ca key/crt name" "serial" "days"
gen_rsa_key_csr()
{
	OPENSSL_CONFIG_FILENAME="openssl.conf"
	write_openssl_config_file $OPENSSL_CONFIG_FILENAME $1 2048

	openssl req -out $1.csr -newkey rsa:2048 -nodes -keyout $1.key \
		-subj "/C=$COUNTRY/ST=$STATE/L=$LOCALITY/O=$ORGANIZATION/OU=$ORGANIZATION/CN=$2" -config $OPENSSL_CONFIG_FILENAME

	openssl x509 -req -days $5 -in $1.csr -CA $3.crt -CAkey $3.key -set_serial $4 -out $1.crt \
		-extensions req_ext -extfile $OPENSSL_CONFIG_FILENAME

	rm $OPENSSL_CONFIG_FILENAME
}
# gen_ecc_key_csr "out files name" "certificate CN"
gen_ecc_key_csr()
{
	OPENSSL_CONFIG_FILENAME="openssl.conf"
	write_openssl_config_file $OPENSSL_CONFIG_FILENAME $1 384

	openssl req -out $1.csr -newkey ec:<(openssl ecparam -name secp384r1) -nodes -keyout $1.key \
		-subj "/C=$COUNTRY/ST=$STATE/L=$LOCALITY/O=$ORGANIZATION/OU=$ORGANIZATION/CN=$2" -config $OPENSSL_CONFIG_FILENAME

	openssl x509 -req -days $5 -in $1.csr -CA $3.crt -CAkey $3.key -set_serial $4 -out $1.crt \
		-extensions req_ext -extfile $OPENSSL_CONFIG_FILENAME

	rm $OPENSSL_CONFIG_FILENAME
}
# gen_pkcs12 "in/out key/crt/p12 name"
gen_pkcs12()
{
	openssl pkcs12 -export  -inkey $1.key -in $1.crt -certfile $CA_NAME.crt -out $1.p12
}
# gen_oadr20b_fingerprint "in/out crt/fingerprint name"
gen_oadr20b_fingerprint()
{
	openssl x509 -in $1.crt -fingerprint -sha256 -noout | cut -d'=' -f2 | cat | tail -c 30 > $1.fingerprint
}
# gen_oadr20a_fingerprint "in/out crt/fingerprint name"
gen_oadr20a_fingerprint()
{
	openssl x509 -in $1.crt -fingerprint -sha1 -noout | cut -d'=' -f2 | cat | tail -c 30 > $1.fingerprint.oadr20a
}
# gen_selfsigned_key_crt "out key/crt name"
gen_selfsigned_key_crt()
{
	openssl req -nodes -new -x509  -keyout $1.key -out $1.crt \
		-subj "/C=$COUNTRY/ST=$STATE/L=$LOCALITY/O=$ORGANIZATION/OU=$ORGANIZATION/CN=$1"
}
###################################
# CA
###################################
gen_selfsigned_key_crt $CA_NAME

###################################
# VTN RSA
###################################
gen_rsa_key_csr $VTN_NAME-rsa $VTN_NAME $CA_NAME 01 365
gen_oadr20b_fingerprint $VTN_NAME-rsa
gen_oadr20a_fingerprint $VTN_NAME-rsa

###################################
# VTN ECC
###################################
gen_ecc_key_csr $VTN_NAME-ecc $VTN_NAME $CA_NAME 02 365
gen_oadr20b_fingerprint $VTN_NAME-ecc
gen_oadr20a_fingerprint $VTN_NAME-ecc

###################################
# CLIENT
###################################
gen_rsa_key_csr $CLIENT_NAME $CLIENT_NAME $CA_NAME 03 365
gen_pkcs12 $CLIENT_NAME

###################################
# VEN1
###################################
gen_rsa_key_csr $VEN1_NAME $VEN1_NAME $CA_NAME 04 365
gen_oadr20b_fingerprint $VEN1_NAME
gen_oadr20a_fingerprint $VEN1_NAME

###################################
# VEN2
###################################
gen_ecc_key_csr $VEN2_NAME $VEN2_NAME $CA_NAME 05 365
gen_oadr20b_fingerprint $VEN2_NAME
gen_oadr20a_fingerprint $VEN2_NAME





