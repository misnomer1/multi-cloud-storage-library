# multi-cloud-storage-lib
Library for cloud storage that supports
1. Multi Cloud Data Storage writes to Amazon, Microsoft and Google Clouds
2. Compression via Gzip, zstd at compile and runtime
3. Encryption using Amazon KMS, Google KMS and Microsoft Identity

# Multi-Cloud Storage Service

## Overview
This project provides a unified interface for interacting with multiple cloud storage providers, including Amazon S3, Google Cloud Storage, and Microsoft Azure Blob Storage. It simplifies working with cloud storage by abstracting provider-specific details, allowing users to upload and download files from multiple clouds using a single interface.

Advantages of Using Multi-Cloud Storage Service
Cost Savings through Compression
Our multi-cloud storage service integrates advanced compression techniques to significantly reduce the size of your files before uploading them to the cloud. By minimizing file sizes, we help you save on storage costs across different cloud providers. Smaller files not only occupy less space but also reduce the bandwidth required for uploads and downloads, further contributing to cost efficiency, especially when dealing with large volumes of data or frequent data transfers.

Enhanced Security with Encryption
Security is paramount when storing and transferring data across the cloud. Our service ensures that your data is encrypted before it leaves your premises, providing an added layer of security. By using robust encryption standards, we protect your data from unauthorized access, making it suitable for storing sensitive information. This encryption is seamless and integrated, ensuring that data confidentiality is maintained without compromising on user experience or data accessibility.

The Importance of a Multi-Cloud Strategy
Embracing a multi-cloud strategy is becoming increasingly important for businesses seeking to enhance their data resilience, avoid vendor lock-in, and optimize costs. Our multi-cloud storage service is designed with this strategy in mind, offering you the flexibility to interact with multiple cloud storage providers through a unified interface. This approach not only ensures business continuity by diversifying your storage options but also enables you to leverage the unique strengths and pricing models of different providers. By distributing your data across various clouds, you can achieve optimal performance, comply with regional data storage regulations, and enhance your negotiation leverage with cloud service providers.

In summary, our multi-cloud storage service is not just a tool for simplifying cloud storage interactions; it's a comprehensive solution that addresses key business concerns such as cost, security, and strategic flexibility. By integrating our service into your data management workflows, you can achieve efficient, secure, and cost-effective data storage across the leading cloud platforms.



## Features
- Upload files to Amazon S3, Google Cloud Storage, and Azure Blob Storage simultaneously.
- Download files from a specified cloud storage provider.
- Extendable to support additional cloud storage providers.

## Prerequisites
- Java JDK 8 or higher.
- Maven for dependency management.
- Access credentials for Amazon AWS, Google Cloud Platform, and Microsoft Azure.

## Setup
### Configuration
1. **AWS S3**: Ensure you have an AWS account, access to an S3 bucket, and the necessary permissions. Configure your AWS credentials using the AWS CLI or by setting environment variables.

2. **Google Cloud Storage**: Set up a Google Cloud project, create a storage bucket, and obtain authentication credentials. Set the `GOOGLE_APPLICATION_CREDENTIALS` environment variable to the path of your service account key file.

3. **Azure Blob Storage**: Create an Azure account, set up a storage account and container, and obtain your connection string.

### Dependencies
Add the following dependencies to your `pom.xml`:

```xml
<!-- AWS SDK for Java -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>LATEST_VERSION</version>
</dependency>

<!-- Google Cloud Storage -->
<dependency>
    <groupId>com.google.cloud</groupId>
    <artifactId>google-cloud-storage</artifactId>
    <version>LATEST_VERSION</version>
</dependency>

<!-- Azure Blob Storage -->
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-storage-blob</artifactId>
    <version>LATEST_VERSION</version>
</dependency>
```
## Usage
Uploading Files
To upload a file to all configured cloud providers:
```xml
MultiCloudStorageService service = new MultiCloudStorageService(awsClient, googleClient, azureClient);
service.putObject(bucketName, objectKey, fileData, compressType);
```
##Downloading Files
To download a file from a specific cloud provider:
```xml
byte[] fileData = service.getObject(CloudProvider.AMAZON, bucketName, objectKey);
```
## Contributing
Contributions to extend the functionality, improve efficiency, or fix bugs are welcome. Please follow the standard fork-and-pull request workflow.

Fork the repository.
Create a new branch for your feature or fix.
Commit your changes.
Push to your fork and submit a pull request.

## License
MIT License - see the LICENSE file for details.