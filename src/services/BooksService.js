import axios from 'axios';

export default {

  getBooks() {
    return axios.get('/books')
  },

  getUserBooks(id) {
    return axios.get(`/books/user/${id}`)
  },
  
  submitBook(book) {
    return axios.post('/books', book)
  },

  getBookById(id) {
    return axios.get(`/books/${id}`)
  },

  getUserBookById(id, userId) {
    return axios.get(`/books/${id}/user/${userId}`)
  },

  getRecommendedBook(userId) {
    return axios.get(`/books/recommended/user/${userId}`)
  },

  setBookCompletion(bookId, completed, userId) {
    return axios.patch(`/books/${bookId}?completed=${completed}&userId=${userId}`)
  }
}