cd /opt/atlassian/pipelines/agent/build
echo "working on the build setup"
cp -r /opt/atlassian/pipelines/agent/build/bin/custom /opt/hybris/bin/
#cd /opt/hybris/bin/custom/gpcommerce
cd /opt/hybris/config/
mv local.properties bak_local.propertis
mv localextensions.xml bak_localextensions.xml
cp -r /opt/atlassian/pipelines/agent/build/config/dev/ /opt/hybris/config/
echo "Completed with all setup process"
echo "Triggering build"
cd /opt/hybris/bin/platform
. ./setantenv.sh
ant clean all
echo "*********** Creating Production ready code ***********"
ant production
