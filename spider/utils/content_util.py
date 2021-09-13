import re


def format_content(string):
    p = re.compile('\s+')
    string = re.sub(p, ' ', string)
    p1 = re.compile(',+')
    string = re.sub(p1, ' ', string)
    return string
