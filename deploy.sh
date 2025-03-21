#!/bin/bash

# Set the Azure environment variables
if [ "$1" == "development" ]; then
  APP_ENV="dev"
  RESOURCE_GROUP="my-resource-group-dev"
  CONTAINER_APP="my-container-app-dev"
  IMAGE_TAG="dev"
elif [ "$1" == "qa" ]; then
  APP_ENV="test"
  RESOURCE_GROUP="my-resource-group-test"
  CONTAINER_APP="my-container-app-test"
  IMAGE_TAG="test"
elif [ "$1" == "main" ]; then
  APP_ENV="prod"
  RESOURCE_GROUP="my-resource-group-prod"
  CONTAINER_APP="my-container-app-prod"
  IMAGE_TAG="prod"
else
  echo "Invalid environment specified: $1"
  exit 1
fi

# Log in to Azure (assumes Azure CLI is already installed and configured)
echo "Logging in to Azure..."
az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET --tenant $AZURE_TENANT_ID

# Build and push the Docker image to Azure Container Registry
echo "Building and pushing the Docker image..."
az acr login --name myregistry
docker build -t myregistry.azurecr.io/my-app:$IMAGE_TAG .
docker push myregistry.azurecr.io/my-app:$IMAGE_TAG

# Deploy the updated container image to the Azure Container App
echo "Deploying to Azure Container App..."
az containerapp update \
  --name $CONTAINER_APP \
  --resource-group $RESOURCE_GROUP \
  --image myregistry.azurecr.io/my-app:$IMAGE_TAG

# Verify the deployment
echo "Deployment to $APP_ENV environment completed!"



# Log in to Docker Hub
#if images are pushed to docker hub
echo "Logging in to Docker Hub..."
docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD

# Build and push the Docker image to Docker Hub
echo "Building and pushing the Docker image..."
docker build -t my-dockerhub-repo/my-app:$IMAGE_TAG .
docker push my-dockerhub-repo/my-app:$IMAGE_TAG

# Deploy the updated container image to Azure Container App
echo "Deploying to Azure Container App..."
az containerapp update \
  --name $CONTAINER_APP \
  --resource-group $RESOURCE_GROUP \
  --image my-dockerhub-repo/my-app:$IMAGE_TAG

# Verify the deployment
echo "Deployment to $APP_ENV environment completed!"