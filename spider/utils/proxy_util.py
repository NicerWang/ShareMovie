import requests
import json

url = "http://47.93.47.13:5901/get/"


def get_proxy_by_url(url):
    for i in range(20):
        try:
            web_data = requests.get(url=url)
            if web_data.status_code == 200:
                json_text = web_data.text
                obj = json.loads(json_text)
                if obj['https'] is True:
                    return {
                        'http': "https://" + obj['proxy']
                    }
                else:
                    return{
                        'http': "http://" + obj['proxy']
                    }
        except:
            pass


def get_proxy():
    return get_proxy_by_url(url)
