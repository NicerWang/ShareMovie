import csv
import pickle
from node import Node


def form_matrix_raw():
    # Array idx => Movie ID
    movie_index_map = []
    # Array idx => User ID
    user_index_map = []
    # User ID => Array idx
    user_reverse_map = {}
    # rating matrix
    matrix = []

    user_idx = -1
    movie_idx = -1

    for i in range(1, 67):
        with open("data/" + str(i) + ".csv", encoding="utf-8") as f:
            reader = csv.reader(f)
            is_movie_id_filled = False
            for r in reader:
                if not is_movie_id_filled:
                    movie_idx = movie_idx + 1
                    is_movie_id_filled = True
                    movie_index_map.append(r[0])
                if user_reverse_map.get(r[1]) is None:
                    user_idx = user_idx + 1
                    user_index_map.append(r[1])
                    user_reverse_map[r[1]] = user_idx
                matrix.append(Node(movie_idx, user_reverse_map.get(r[1]), int(r[2])))

    with open("matrix_raw.pickle", "wb") as f:
        pickle.dump(matrix, f)
    with open("movie_index_map.pickle", "wb") as f:
        pickle.dump(movie_index_map, f)
    with open("user_index_map_raw.pickle", "wb") as f:
        pickle.dump(user_index_map, f)
    with open("user_reverse_map_raw.pickle", "wb") as f:
        pickle.dump(user_reverse_map, f)


if __name__ == '__main__':
    form_matrix_raw()
