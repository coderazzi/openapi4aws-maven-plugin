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
              <audience>
                <id>2f0m9fcoiejij4316u574aq259</id>
                <id>7ac34sujrb8gmvj2b6blpi7ruu</id>
              </audience>
              <authorization-type>oauth2</authorization-type>
            </authorizer>
            <authorizer>
              <name>Unused</name>
              <identity-source>$request.header.Authorization</identity-source>
              <issuer>https://cognito-idp.eu-west-2.amazonaws.com/eu-west-2_1T9bfKHNp</issuer>
              <audience>2f0m9fcoiejij4316u574aq259</audience>
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
              <input>use.yaml</input>
              <glob-input>*.properties</glob-input>
              <output-folder>target</output-folder>
            </transform>
            <transform>
              <input>
                <filename>use.yaml</filename>
                <filename>other.yaml</filename>
              </input>
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

It is possible to define this long XML configuration in one or more external files, or just using 
a mix of in-pom definitions and external property files. For example:

    <configuration>
      <external>openapi4aws.settings</external>
      <transforms>
        <transform>
          <input>use.yaml</input>
        </transform>
      </transforms>
    </configuration>

The external file defines the configuration values using the openapi4aws cli configuration:
https://github.com/coderazzi/openapi4aws 
. An example of such configuration file is:

    # a comment
    authorizer.name=DubaixCognito,Other
    authorizer.identity-source=$request.header.Authorization
    authorizer.audience=2f0m9fcoiejij4316u574aq259,7ac34sujrb8gmvj2b6blpi7ruu
    authorizer.issuer=https://cognito-idp.eu-west-2.amazonaws.com/eu-west-2_1T9bfKHNp

    tag.Frontend=http://3.64.241.104:12121/tmp/,DubaixCognito,user.email,user.id
    path.user.scope2=http://OTHER_PATH:12122/path,Other,user.email

