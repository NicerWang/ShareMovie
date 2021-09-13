import csv
import codecs


def write_data_to_file(file_path, *args):
    with codecs.open(file_path, 'a', 'utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(args)


def write_img(file_path, content):
    with open(file_path, 'ab') as f:
        f.write(content)


def write_data_to_txt_file(file_path, content):
    with codecs.open(file_path, 'a', 'utf-8') as f:
        f.write(content + '\n')