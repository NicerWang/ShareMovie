import pickle
from node import Node




def take(n):
    return n.u


def find_good_users():
    user_cnt_map = {}
    with open('matrix_raw.pickle', 'rb') as f:
        matrix = pickle.load(f)
    for i in matrix:
        if user_cnt_map.get(i.u) is None:
            user_cnt_map[i.u] = 1
        else:
            user_cnt_map[i.u] = user_cnt_map[i.u] + 1

    good_user = []

    for i in user_cnt_map.items():
        if i[1] > 5:
            good_user.append(i[0])
    with open('good_user.pickle', 'wb') as gu:
        pickle.dump(good_user, gu)

    good_user_id = []
    with open('good_user.pickle', 'rb') as gup:
        good_user = pickle.load(gup)
        with open('user_index_map_raw.pickle', 'rb') as uim:
            user_index_map = pickle.load(uim)
            for i in good_user:
                good_user_id.append(user_index_map[i])
    with open('good_user_id.pickle', 'wb') as gui:
        pickle.dump(good_user_id, gui)


if __name__ == '__main__':
    find_good_users()
