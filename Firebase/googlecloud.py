import logging

from google.cloud import storage
import os,stat
import glob

os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = 'smart-gate-system-1405-1f5dc5593ce8.json'

storage_client = storage.Client()
bucket_name = 'smart_gate'
bucket = storage_client.get_bucket(bucket_name)

# blob = bucket.blob('frame0.jpg')
# blob.upload_from_filename('./11/frame0.jpg')

id = 12
idS = str(id)
local_path = "./" + idS + "/"
gcs_path = idS + "/"


def upload_local_directory_to_gcs(local_path, bucket, gcs_path):
    for local_file in glob.glob(local_path + '/**'):
        if not os.path.isfile(local_file):
            upload_local_directory_to_gcs(local_file, bucket, gcs_path + "/" + os.path.basename(local_file))
        else:
            remote_path = os.path.join(gcs_path, local_file[len(local_path):])
            blob = bucket.blob(remote_path)
            blob.upload_from_filename(local_file)




destination_file_name = "./ImagesFromCloud"
os.mkdir(destination_file_name)
os.chmod(destination_file_name,stat.S_IRWXG)
source_blob_name = idS


def download_blob(bucket_name, source_blob_name, destination_file_name):
    """Downloads a blob from the bucket."""
    # The ID of your GCS bucket
    # bucket_name = "your-bucket-name"

    # The ID of your GCS object
    # source_blob_name = "storage-object-name"

    # The path to which the file should be downloaded
    # destination_file_name = "local/path/to/file"

    # Construct a client side representation of a blob.
    # Note `Bucket.blob` differs from `Bucket.get_blob` as it doesn't retrieve
    # any content from Google Cloud Storage. As we don't need additional data,
    # using `Bucket.blob` is preferred here.
    blob = bucket.blob(source_blob_name)
    blob.download_to_filename(destination_file_name)

    print(
        "Downloaded storage object {} from bucket {} to local file {}.".format(
            source_blob_name, bucket_name, destination_file_name
        )
    )


# upload_local_directory_to_gcs(local_path, bucket, gcs_path)
#download_blob(bucket_name, source_blob_name, destination_file_name)



logging.basicConfig(format='%(levelname)s:%(message)s', level=logging.DEBUG)
storage_client = storage.Client.from_service_account_json('smart-gate-system-1405-1f5dc5593ce8.json')


blobs=bucket.list_blobs(prefix=idS, delimiter='/')
folder='/google-cloud/smart_gate/{}'.format(idS)
# Download the file to a destination
def download_to_local():
    logging.info("File download Started…. Wait for the job to complete.")
    # Create this folder locally if not exists
    if not os.path.exists(folder):
        os.makedirs(folder)
     # Iterating through for loop one by one using API call
    for blob in blobs:
        logging.info('Blobs: {}'.format(blob.name))
        destination_uri = '{}/{}'.format(folder, blob.name)
        blob.download_to_filename(destination_uri)
        logging.info('Exported {} to {}'.format(
        blob.name, destination_uri))




download_to_local()