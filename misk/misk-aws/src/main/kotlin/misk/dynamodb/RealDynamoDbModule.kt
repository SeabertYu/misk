package misk.dynamodb

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig
import com.google.inject.Provides
import misk.cloud.aws.AwsRegion
import misk.inject.KAbstractModule
import javax.inject.Singleton

/**
 * Install this module to have access to an AmazonDynamoDB client. This can be
 * used to create a DynamoDbMapper for querying of a DynamoDb table.
 */
class RealDynamoDbModule : KAbstractModule() {
  override fun configure() {
    requireBinding<AWSCredentialsProvider>()
    requireBinding<AwsRegion>()
  }

  @Provides @Singleton
  fun providesAmazonDynamoDB(
    awsRegion: AwsRegion,
    awsCredentialsProvider: AWSCredentialsProvider
  ): AmazonDynamoDB {
    return AmazonDynamoDBClientBuilder
        .standard()
        .withRegion(awsRegion.name)
        .withCredentials(awsCredentialsProvider)
        .build()
  }
}
