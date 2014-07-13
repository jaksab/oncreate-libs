## Global

This tool allows you to quickly and flexibly configure and perform HTTP requests.
Create by Konovalenko Andrew, onCreate company.

## How to use?

- If you want use this project as library:

check >> Project - Properties - Android - is Library

- Code example:

```
Net net = new Net(context, Net.METHOD_GET, "https://api.github.com");

net.connect(new ConnectionListener() {
			
			@Override
			public void onStartConnection(String toUrl) {
				
			}
			
			@Override
			public void onFinishConnection(boolean isSuccessful, HttpResponse response,
					String entity, String fromUrl) {
				
			}
		});
```
void onStartConnection - called before library send request
[String toUrl] - the last address of the request

void onFinishConnection( - called after library get response
[String toUrl] - the last address of the request
[String toUrl] - the last address of the request
[String toUrl] - the last address of the request


See more in DemonstrationActivity in this project.
