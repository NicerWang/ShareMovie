import random
import time

import requests
from bs4 import BeautifulSoup

from utils import file_utils as fu
from utils import user_agent_util as uau, proxy_util as pu

file_path_info = ""
file_path_pic = ""


def get_infos(url, in_file_path_info, in_file_path_pic):
    global file_path_pic,file_path_info
    file_path_info = in_file_path_info
    file_path_pic = in_file_path_pic

    time.sleep(random.random() * 12 + 7.3)

    for i in range(20):
        web_data = requests.get(url=url, headers=uau.get_header(), proxies=pu.get_proxy())
        if web_data.status_code == 200:
            html_text = web_data.text
            document = BeautifulSoup(html_text, 'html.parser')

            (movie_name,
             movie_on_time,
             movie_director,
             movie_screenwriter,
             movie_actors,
             movie_type) = get_movie_info(document)

            fu.write_data_to_file(file_path_info,
                                  movie_name,
                                  movie_director,
                                  movie_screenwriter,
                                  movie_actors,
                                  movie_type,
                                  movie_on_time,)
            image = get_image(document)
            fetch_img(image)
            return

    raise Exception("Unable to get data for too many trials")


def get_movie_info(document):
    movie_info = document.select_one('#info')
    # movie name
    movie_name_span = document.select('#content > h1 > span')[0]
    movie_name = movie_name_span.get_text().strip()
    # movie on time & genre
    movie_on_time = ''
    movie_type = []
    spans = movie_info.select('span')
    for span in spans:
        if span.get('property') is None:
            continue
        if "v:initialReleaseDate" in span.get('property'):
            movie_on_time = span.get_text().strip()
        if "v:genre" in span.get('property'):
            movie_type.append(span.get_text().strip())
    if movie_on_time.find('(') != -1:
        movie_on_time = movie_on_time.split('(')[0]
    # movie staff
    movie_director, movie_screenwriter, movie_actors, movie_type = [], [], [], []
    movie_roles = movie_info.select('span > span.attrs > a')
    for movie_role in movie_roles:
        if movie_role.get('rel') is None:
            movie_screenwriter.append(movie_role.get_text().strip())
        elif "v:directedBy" in movie_role.get('rel'):
            movie_director.append(movie_role.get_text().strip())
        elif "v:starring" in movie_role.get('rel'):
            movie_actors.append(movie_role.get_text().strip())

    return (movie_name, movie_on_time, ' / '.join(movie_director), ' / '.join(movie_screenwriter),
            ' / '.join(movie_actors), ' / '.join(movie_type))


def get_image(document):
    img = document.select_one('#mainpic > a > img')
    img_url = img.get('src')
    return img_url


def fetch_img(img_url):
    time.sleep(random.random() * 10 + 6.7)

    for i in range(20):
        web_data = requests.get(url=img_url, headers=uau.get_header(), proxies=pu.get_proxy())
        print(img_url)
        if web_data.status_code == 200:
            img_data = web_data.content
            fu.write_img(file_path_pic, img_data)
            return

    raise Exception("Unable to get data for too many trials")
