import sys
import os
import info_spider

if __name__ == '__main__':
    argv = sys.argv[1:]

    if not os.path.exists(argv[0]):
        os.mkdir(argv[0])

    if len(argv) < 2:
        argv.append('-1')

    # get movie info
    movie_info_page_url = "https://movie.douban.com/subject/" + argv[0] + "/?from=showing"
    file_path_info = os.path.join(os.getcwd(), argv[0], 'info.csv')
    file_path_pic = os.path.join(os.getcwd(), argv[0], 'face.png')
    info_spider.get_infos(movie_info_page_url, file_path_info, file_path_pic)
    # except:
    #     print("Fail^-1")
