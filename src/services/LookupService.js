import axios from 'axios';

export default {

  getBookInfoByIsbn(isbn) {
    return axios.get(`/lookup/isbn/${isbn}`)
  }
}