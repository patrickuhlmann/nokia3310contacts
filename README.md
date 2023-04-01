# nokia3310contacts
Convert contacts (vcf) from Nextcloud (v4) to Nokia 3310 format (v2.1)

This is a small private project used for a one time migration from Nextcloud contacts to my Nokia 3310 phone.

A description of the whole process can be found here:
https://www.uhlme.ch/nokia/3310/nextcloud/2021/03/20/nokia-3310-3G-nextcloud-contacts.html

In the doc folder you find a reference of the vcf 2.1 format

In the example folder you find a small example vcf file (anonymized from a real export) that you can use for testing purposes

As this is just a very small command line - throw away - tool, I didn't bother writing unit-tests.

## Usage:
The program takes two parameters. Firs the input file (that you downloaded from nextcloud), second the output file (that can be copied to nokia).

On the command line run:

```
./gradlew run --args='example/contacts.vcf example/contacts_out.vcf'
```

## Troubleshooting

I used Java 17 (LTS) to run it.

If you get an error similar than:

```
General error during semantic analysis: Unsupported class file major version 57
java.lang.IllegalArgumentException: Unsupported class file major version 57
```

It is most likely that you don't run Java 17


