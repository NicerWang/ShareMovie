import pickle
import csv

import numpy as np
import numpy.linalg as lg
from node import Node

movie_size = 66
user_size = 1028
limit = 10


def normalize(mat):
    dig = []
    for i in range(0, movie_size):
        dig.append(np.sum(mat[i, :]))
    for i in range(movie_size):
        for j in range(movie_size):
            mat[i,j] = mat[i,j] / dig[i]
    return mat


def iteration(R, iters, alpha, norm):
    F = R
    for i in range(0, iters):
        F = alpha * np.dot(norm, F) + (1 - alpha) * R
    return F


def get_rank(data, movies, scores, uid):
    R = np.zeros((movie_size, 1));
    for j in range(0, movie_size):
        for k in range(0, len(movies)):
            if j == movies[k] and data[uid,j] > 1:
                R[j] = R[j] + data[uid,j]
    R = R / np.sum(R)
    return R;


def get_rec_movie_id(uid):
    raw_movie_relation = None
    raw_user_prefer = np.ones((movie_size,user_size))
    with open('matrix.pickle','rb') as f:
        matrix = pickle.load(f)
        for i in matrix:
            raw_user_prefer[i.m][i.u] = i.s / 3 + 0.1
    with open('movie_relation.pickle','rb') as f:
        raw_movie_relation = pickle.load(f)
    movie_relation = normalize(raw_movie_relation)
    raw_user_prefer = raw_user_prefer.transpose()
    vectorQuery = raw_user_prefer[uid, :];
    loved_movies = []
    loved_scores = []
    for idx in range(0, len(vectorQuery)):
        if vectorQuery[idx] > 1:
            loved_movies.append(idx)
            loved_scores.append(vectorQuery[idx])
    rank = get_rank(raw_user_prefer, loved_movies, loved_scores, uid)
    i = iteration(rank, 100, 0.75, movie_relation)
    argsort = np.argsort(i, axis=0)
    movie_rec = []
    for idx in argsort:
        if raw_user_prefer[uid,idx[0]] == 1:
            movie_rec.append(idx)
    return movie_rec[:limit]

if __name__ == '__main__':
    movie_index_map = None
    user_index_map = None

    with open('movie_index_map.pickle','rb') as f:
        movie_index_map = pickle.load(f)
    with open('user_index_map.pickle','rb') as f:
        user_index_map = pickle.load(f)
    with open('res.csv','w') as f:
        writer = csv.writer(f)
        for i in range(1028):
            movies = get_rec_movie_id(i)
            res = []
            res.append(user_index_map[i])
            for movie in movies:
                res.append(movie_index_map[movie[0]]);
            writer.writerow(res)




