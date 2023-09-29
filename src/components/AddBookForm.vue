<template>
  <form id="add-book-form" autocomplete="off" @submit.prevent="submitBook">
    <div class="alert-msg" role="alert" v-if="addBookErrors">
      {{ addBookErrorMsg }}
    </div>
    
    <div class="form-input-group">
      <label for="isbn">ISBN</label>
      <input type="text" id="isbn" v-model="book.isbn" :disabled="disableForm"/>
    </div>
    <div class="form-input-group">
      <button type="button" @click.prevent="isbnLookup()" :disabled="disableForm">Look Up Title and Author By ISBN</button>
    </div>
    <div class="form-input-group">
      <label for="title">Book Title</label>
      <input type="text" id="title" v-model="book.title" :disabled="disableForm"/>
    </div>
    <div class="form-input-group">
      <label for="author">Author</label>
      <input type="text" id="author" v-model="book.author" :disabled="disableForm"/>
    </div>
    <div class="form-input-group" v-if="$store.state.user.authorities[0].name == 'ROLE_PARENT'">
      <label for="parent-only">Acceptable For Children</label>
      <input type="checkbox" id="parent-only" v-model="book.forChildren" :disabled="disableForm"/>
    </div>
    <div class="form-input-group">
      <button type="submit" :disabled="disableForm">Submit Book</button>
    </div>
  </form>
</template>

<script>
import booksService from "../services/BooksService";
import lookupService from "../services/LookupService";

export default {
  data() {
    return {
      book: {
        title: "",
        author: "",
        isbn: "",
        forChildren: true
      },
      disableForm: false,
      addBookErrors: false,
      addBookErrorMsg: "",
    };
  },
  methods: {
    submitBook() {
      this.book.title = this.book.title.trim();
      this.book.author = this.book.author.trim();
      this.formatIsbn();
      if (this.book.title === "") {
        this.addBookErrors = true;
        this.addBookErrorMsg = "Please enter book title.";
      } else if ( !(this.book.isbn.length == 0 || this.book.isbn.length == 10 || this.book.isbn.length == 13 )) {
        this.addBookErrors = true;
        this.addBookErrorMsg = "ISBNs are 10 or 13 digits long.";
      } else {
        this.addBookErrorMsg = "";
        this.addBookErrors = false;
        this.disableForm = true;
        booksService.submitBook(this.book).then((response) => {
          if (response.status == 201) {
            //Clear form for next entry
            this.book = {
              title: "",
              author: "",
              isbn: "",
              forChildren: true
            };
            this.disableForm = false;
            this.$emit('create-book');
          }
           }).catch( error => {
          if (error.response.status == 500) {
            this.addBookErrors = true;
            this.addBookErrorMsg = "Error creating book.";
            }
            this.disableForm = false;
        });
      }
    },
    isbnLookup() {
      this.formatIsbn();
      if (this.book.isbn.length == 10 || this.book.isbn.length == 13) {
        lookupService.getBookInfoByIsbn(this.book.isbn).then( response => {
          if (response.status == 200) {
            this.book.title = response.data.title;
            this.book.author = response.data.author;
          }
        }).catch( error => {
          if (error.response.status == 500) {
            this.addBookErrors = true;
            this.addBookErrorMsg = "Error looking up ISBN.";
          }
        });
      } else {
        this.addBookErrors = true;
        this.addBookErrorMsg = "ISBNs are 10 or 13 digits long.";
      }
    },
    formatIsbn() {
      this.book.isbn = this.book.isbn.replace(/\D/g, '');
    }
  },
};
</script>

<style scoped>
#add-book-form {
  margin-top: 0.5em;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-items: flex-end;
  font-weight: bold;
}
.form-input-group {
  margin: 0.3em;
  display: flex;
  align-items: center;
  margin-bottom: 1em;
  border: black 2px;

}
label {
  margin-right: 0.5em;
}

button {
  margin-top: 0.5em;
  margin-bottom: 0.5em;
  padding: 0.3em;
  padding-left: 0.6em;
  padding-right: 0.6em;
  border: 2px solid black;
  border-radius: 8px;
  background-color: #31b4c9;
  font-weight: bold;
  color: white;
}
button:hover {
  background-color: #1590a3;
}
</style>