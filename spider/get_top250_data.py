import requests
from bs4 import BeautifulSoup
from utils import user_agent_util as ug
from utils import proxy_util as pu
from utils import file_utils as fu
import time
import random
import os


def get(url, order):
    page_num = 1
    next_url = get_data(url, order)
    while next_url is not None:
        url = url.split('?')[0] + next_url
        next_url = get_data(url, order)
        page_num += 1
        if page_num >= 100:
            return


def get_data(url, order):
    for i in range(20):
        web_data = requests.get(url=url, headers=ug.get_header(), proxies=pu.get_proxy())
        if web_data.status_code == 200:
            time.sleep(random.random()+1.2)
            html_text = web_data.text
            document = BeautifulSoup(html_text, 'html.parser')
            all_review_divs = document.select("#content > div > div.article > div.review-list > div")
            if all_review_divs is None:
                return

            for all_review_div in all_review_divs:
                # for folded comments
                if all_review_div.get('class') is not None and all_review_div.get('class')[0].find('fold-hd') != -1:
                    continue
                if all_review_div.get('class') is not None and all_review_div.get('class')[0].find('fold-bd') != -1:
                    continue
                write_review(all_review_div, url, order)

            button = document.select_one('#content > div > div.article > div.paginator > span.next')
            if button is None:
                return None
            page_a = button.select_one('a')
            if page_a is not None:
                return page_a.get('href')
            else:
                return None


def write_review(all_review_div, url, order):
    # movie_id
    movie_id = url.split('/')[-2]

    # user_id
    user_id_a = all_review_div.select_one("div > header > a.avator")
    href = user_id_a.get("href")
    user_id = href.split('/')[-2].strip()

    # star
    star_span = all_review_div.select_one("div > header > span.main-title-rating")
    if star_span is None:
        return
    elif star_span.get('class')[0].find('5') != -1:
        star = 5
    elif star_span.get('class')[0].find('4') != -1:
        star = 4
    elif star_span.get('class')[0].find('3') != -1:
        star = 3
    elif star_span.get('class')[0].find('2') != -1:
        star = 2
    else:
        star = 1
    fu.write_data_to_file(os.path.join(os.getcwd(), 'data', str(order) + '.csv'),
                          movie_id,
                          user_id,
                          star)


def get_top250_urls():
    urls = []
    for i in range(10):
        url = 'https://movie.douban.com/top250?start=' + str(i * 25)
        for j in range(20):
            web_data = requests.get(url=url, headers=ug.get_header(), proxies=pu.get_proxy())
            if web_data.status_code == 200:
                time.sleep(random.random())
                html_text = web_data.text
                document = BeautifulSoup(html_text, 'html.parser')
                # #content > div > div.article > ol > li > div > div.info > div.hd > a
                a_s = document.select('#content > div > div.article > ol > li > div > div.info > div.hd > a')
                for a in a_s:
                    urls.append(a.get('href') + 'reviews')
                print(urls)
                break
    return urls


if __name__ == '__main__':
    urls = get_top250_urls()
    for i in range(49, len(urls)):
        get(urls[i], i + 1)
