import random
import time

import requests
from bs4 import BeautifulSoup

from utils import file_utils as fu, content_util as cu
from utils import user_agent_util as uau, proxy_util as pu

num = 0
invalidate_terms = 0
earliest_time = float("inf")
date = 0
file_path_rate = ""
file_path_content = ""


def get(url, in_file_path_rate, in_file_path_content, in_date):
    global num, earliest_time, invalidate_terms, date, file_path_content, file_path_rate
    date = in_date
    file_path_rate = in_file_path_rate
    file_path_content = in_file_path_content
    global invalidate_terms
    next_url = get_review(url)
    while next_url is not None:
        url = url.split('?')[0] + next_url
        next_url = get_review(url)
        if invalidate_terms >= 20:
            return
    return earliest_time


def get_review(url):
    global num
    time.sleep(random.random() * 13 + 5.1)
    for i in range(20):
        web_data = requests.get(url=url, headers=uau.get_header(), proxies=pu.get_proxy())
        if web_data.status_code == 200:
            num += 20
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
                write_review(all_review_div)

            reviews_a = document.select('#content > div > div.article > div.review-list > div > div.main > '
                                        'div.main-bd > h2 > a')
            for a in reviews_a:
                get_content(a)

                if invalidate_terms >= 20:
                    return
            button = document.select_one('#content > div > div.article > div.paginator > span.next')
            if button is None:
                return None
            page_a = button.select_one('a')
            if page_a is not None:
                return page_a.get('href')
            else:
                return None

    raise Exception("Unable to get data for too many trials")


def write_review(all_review_div):
    global invalidate_terms, earliest_time, date
    # user name
    user_name_a = all_review_div.select_one("div > header > a.name")
    user_name = user_name_a.get_text()
    # user id
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
    # time
    date_time_span = all_review_div.select_one("div > header > span.main-meta")
    date_time = date_time_span.get_text()
    time_array = time.strptime(date_time, '%Y-%m-%d %H:%M:%S')
    time_stamp = int(time.mktime(time_array))
    if int(date) > time_stamp:
        invalidate_terms += 1
        return
    if time_stamp < earliest_time:
        earliest_time = time_stamp
    # like
    like_a = all_review_div.select_one("div > div.main-bd > div.action > a.action-btn.up")
    like_span = like_a.select_one('span')
    like = like_span.get_text().strip()
    if like is None or like == '':
        like = '0'
    fu.write_data_to_file(file_path_rate,
                          cu.format_content(user_name),
                          user_id,
                          star,
                          cu.format_content(date_time),
                          like,
                          )


def get_content(a):
    href = a.get('href')
    global earliest_time, invalidate_terms

    time.sleep(random.random() * 7 + 3.8)

    for i in range(20):
        web_data = requests.get(url=href, headers=uau.get_header(), proxies=pu.get_proxy())
        if web_data.status_code == 200:
            html_text = web_data.text
            document = BeautifulSoup(html_text, 'html.parser')

            content = document.select_one('#link-report > div.review-content.clearfix').get_text().strip()

            comment_date_span = document.select_one('#content > div > div.article > div > div.main > header.main-hd > '
                                                    'span.main-meta')
            write_date = comment_date_span.get_text().strip()

            time_array = time.strptime(write_date, '%Y-%m-%d %H:%M:%S')
            time_stamp = int(time.mktime(time_array))

            if int(date) > time_stamp:
                invalidate_terms += 1
                return
            if time_stamp < earliest_time:
                earliest_time = time_stamp

            fu.write_data_to_txt_file(file_path_content, content)
            return

    raise Exception("Unable to get data for too many trials")
