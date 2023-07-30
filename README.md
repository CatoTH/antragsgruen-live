# Real-time communication for Antragsgrün

...



## JWTs for tests

The public / private keys used for the test cases were created using the following commands:
```shell
ssh-keygen -t rsa -b 4096 -m PEM -f bundle.pem
openssl rsa -in bundle.pem -pubout -outform PEM -out jwt-test-public.key
openssl pkcs8 -topk8 -inform PEM -outform PEM -in bundle.pem -out jwt-test-private.key -nocrypt
```