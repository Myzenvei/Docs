cd /NFS_DATA/deployment
mkdir gp_ecom_v0-1
cd gp_ecom_v0-1
touch metadata.properties
echo "package_version=2.3" > metadata.properties
mkdir hybris
cd hybris
mkdir bin
mkdir config
cd bin
cp /opt/hybris_6.6.0/temp/hybris/hybrisServer/hybrisServer-AllExtensions.zip /opt/hybris_6.6.0/temp/hybris/hybrisServer/hybrisServer-Platform.zip .
cd ../config
cp -avr /opt/hybris_6.6.0/temp/hybris/hybrisServer/stag .
 
cd /NFS_DATA/deployment
zip -r gp_ecom_v0-1.zip gp_ecom_v0-1/
md5sum gp_ecom_v0-1.zip > gp_ecom_v0-1.md5
md5sum -c gp_ecom_v0-1.md5
 
cd /opt/ytools/ypackagevalidator2/
./ypackagevalidator2.sh /NFS_DATA/deployment/gp_ecom_v0-1.zip stag 0-1
 
