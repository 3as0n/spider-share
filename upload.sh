#!/bin/sh
gradle clean spider-share-domain:upload  spider-share-api:upload spider-share-integration:upload spider-share-common:upload -x test
gradle clean spider-share-dao:upload  spider-share-service:upload spider-share-web:upload crawler-core:upload -x test



