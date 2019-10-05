import axios from 'axios';

export function getIntroduction() {
    return axios.get('http://localhost:8080/api/introductions');
}

export function deleteIntroduction(no) {
    return axios.delete('http://localhost:8080/api/introductions/' + no)
}

export function editIntroduction (no, data) {
    return axios.put('http://localhost:8080/api/introductions/' + no, data);
}

export function addIntroduction (data) {
    return axios.post('http://localhost:8080/api/introductions', data);
}