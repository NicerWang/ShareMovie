import sys
import os
import review_spider

if __name__ == '__main__':

    argv = sys.argv[1:]

    if not os.path.exists(argv[0]):
        os.mkdir(argv[0])

    if len(argv) < 2:
        argv.append('0')

    movie_info_page_url = "https://movie.douban.com/subject/" + argv[0] + "/?from=showing"

    # get movie reviews
    file_path_comment = os.path.join(os.getcwd(), argv[0], 'comments.csv')
    file_path_content = os.path.join(os.getcwd(), argv[0], 'content.txt')
    comments_url = '/'.join(movie_info_page_url.split('/')[:-1] + ['reviews?sort=time'])
    earliest_time = review_spider.get(comments_url, file_path_comment, file_path_content, argv[1])

    # earliest timestamp
    print("OK^" + str(earliest_time if earliest_time != float("inf") else argv[1]))
