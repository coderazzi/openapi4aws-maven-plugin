# Openapi 4 AWS maven plugin 

* Website: https://coderazzi.net/openapi4aws
* Github: https://github.com/coderazzi/openapi4aws-maven-plugin
* License: MIT license

This is utility to enrich an openapi integration with information specific for the AWS API Gateway.
It allows defining route integrations and authorizers to do automatic (re-)imports in API Gateway.

The openapi integration is extended on two parts:
- security: optionally, adding one or more authorizers.
- paths: extending each method with the associated authorizer and defining an endpoint.

This is a maven plugin for the openapi4aws utility, also referenced in the same website:
* Website: https://coderazzi.net/openapi4aws

And with its own Github repository:
* Github: https://github.com/coderazzi/openapi4aws

As a maven plugin, all the information is passed using the configuration in the pom file.
A configuration example is given below. For the details of each field, 
please refer to the openapi4aws utility (https://github.com/coderazzi/openapi4aws) 

      <plugin>
        <groupId>net.coderazzi</groupId>
        <artifactId>openapi4aws-maven-plugin</artifactId>
        <configuration>
          <authorizers>
            <authorizer>
              <name>DubaixCognito</name>
              <identity-source>$request.header.Authorization</identity-source>
              <issuer>https://cognito-idp.eu-west-2.amazonaws.com/eu-west-2_1T9bfKHNp</issuer>
              <audiences>
                <audience>2f0m9fcoiejij4316u574aq259</audience>
                <audience>7ac34sujrb8gmvj2b6blpi7ruu</audience>
              </audiences>
              <authorization-type>oauth2</authorization-type>
            </authorizer>
          </authorizers>
          <integrations>
            <path-integration>
              <path>/user/scope</path>
              <uri>http://3.64.241.104:12122/path</uri>
              <authorizer>DubaixCognito</authorizer>
              <scopes>
                <scope>user.email</scope>
                <scope>user.id</scope>
              </scopes>
            </path-integration>
            <tag-integration>
              <tag>Frontend</tag>
              <uri>http://3.64.241.104:12121/tmp/</uri>
            </tag-integration>
          </integrations>
          <transforms>
            <transform>
              <filenames>
                <filename>use.yaml</filename>
              </filenames>
              <output-folder>target</output-folder>
            </transform>
          </transforms>
        </configuration>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>transform</goal>
            </goals>
          </execution>
        </executions>
      </plugin>


