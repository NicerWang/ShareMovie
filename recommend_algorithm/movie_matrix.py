import pickle

import jieba
import numpy as np


def form_movie_relation():
    movie_lines = []
    movie_size = 66
    movie_relation = np.zeros((movie_size, movie_size))
    with open('info.csv', 'r') as f:
        line = f.readline()
        while line:
            words = jieba.cut(line, cut_all=False)
            word_list = []
            for word in words:
                if word != '/' and word != ',' and word != '-' and word != ' ' and word != '·' and len(word) > 1:
                    word_list.append(word)
            movie_lines.append(word_list)
            line = f.readline()
    for i in range(0, movie_size):
        for j in range(i, movie_size):
            cnt = 0
            for k in movie_lines[i]:
                if k in movie_lines[j]:
                    cnt = cnt + 1
            for k in movie_lines[j]:
                if k in movie_lines[i]:
                    cnt = cnt + 1
            movie_relation[i][j] = movie_relation[j][i] = cnt
    with open('movie_relation.pickle', 'wb') as f:
        pickle.dump(movie_relation, f)


if __name__ == '__main__':
    form_movie_relation()
