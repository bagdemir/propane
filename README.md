
<img src="http://www.bagdemir.com/img/propane_banner.jpg"/>

##

<img src="https://travis-ci.org/bagdemir/propane.svg?branch=master"/> [![Coverage Status](https://coveralls.io/repos/github/bagdemir/propane/badge.svg?branch=master)](https://coveralls.io/github/bagdemir/propane?branch=master) <img src="https://img.shields.io/packagist/l/doctrine/orm.svg" /> [![GitHub version](https://badge.fury.io/gh/bagdemir%2Fpropane.svg)](https://badge.fury.io/gh/bagdemir%2Fpropane)

Propane is a light-weight Java framework for Configuration Management. This 
project is inspired by the Netflix's Archaius project with some cool new 
features like annotation based configuration entities, custom context 
definitions and so on. If Archaius works for you, probably, you would keep it.
 If you make some considerations on managing your configurations throughout 
 your applications from the scratch, Propane might be the right solution for you.
 
[Check out Project's home for API docs.] (http://propane.moo.io)

## Overview

##

Propane is a Java framework to manage your configurations for different contexts. 
- Annotate your Java classes with configuration management annotations. 
- Define your configurations for each context for a particular application. 

and then  Propane enables the configuration set in applications for this given context.
 
A context may be a region, an environment, or a custom context just defined by you. For instance, if your service is running in the US region and in the dev environment, Propane enables the configurations for Development + US in those applications running in US and dev environment.

## Usage

##

Since the project is under development, you are able to test it by cloning the project on your computer and build it. 

<pre lang="shell">
git clone git@github.com:bagdemir/propane.git
cd propane
mvn -e clean install
</pre>

After you install propane in your local Maven repository, you can add it into your project by defining the dependency and test it:

```
<dependency>
 <groupId>io.moo</groupId>
 <artifactId>propane</artifactId>
 <version>1.0-SNAPSHOT</version>
</dependency>
```

Now, you can create the configuration entities: 

<pre lang="java">
@Configuration(componentId = "test")
@Source(url = "classpath://configurations/test.properties")
public class TestConfigurationEntityWithClasspathSource {

  @KeyValue(name = "testProp")
  private String url;

  @KeyValue(name = "longProp")
  private Long timeout;

  @KeyValue(name = "intProp")
  private int count;

  private String nonPropField;

}
</pre>

You can now register your entity witin the configuration manager:
<pre lang="java">
final ConfigurationManager configurationManager = new ConfigurationManagerImpl();
configurationManager.register(TestConfigurationEntityWithClasspathSource.class);
</pre>

Once the configurations are loaded by the ConfigurationManager, you can access them:

<pre lang="java">
final Optional<TestConfigurationEntityWithClasspathSource> configs = configurationManager.load(TestConfigurationEntityWithClasspathSource.class);
final TestConfigurationEntityWithClasspathSource testPropsWithClasspathSource = configs.get();
testPropsWithClasspathSource.getUrl()
</pre>

That's all. 


## Contributing

##

1. Fork it ( https://github.com/bagdemir/propane/fork )
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create a new Pull Request

## License

##

Copyright (c) 2016 Erhan Bağdemir

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

