<template>
  <main class="books">
    <login-display />
    <div class="box">
      <h1>Books</h1>
    </div> 
    <div>
      <button @click="showAddForm = !showAddForm">
        {{ showAddForm ? 'Cancel' : 'Add Book' }}
      </button>
    </div>
    <add-book-form v-if="showAddForm" 
    @create-book="$store.dispatch('retrieveBooks')" 
    />
    <div class="book-search">
      <input type="search" v-model="searchText" placeholder="Search your library">
    </div>
    <section id="book-display">
      <div v-for="book in displayedBooks" :key="book.id">
        <router-link :to="{ name: 'book', params: { id: book.id } }">
          <div class="book-panel" :class="{ completed: book.completed, 'in-progress': book.lastRead != null }">
            <div class="book-title">
              {{ book.title }}
            </div>
            <div>
              <img class="book-image" :src="bookImgSource(book)" />
            </div> 
            <div class="author">
              {{ book.author }}
            </div>
            <div v-if="$store.state.user.authorities[0].name == 'ROLE_PARENT' && book.forChildren == false">
              🔞 For Adults Only
            </div>
            <div v-if="book.completed" class="completed-text">
              ✔️ Completed!
            </div>
            <div v-if="book.lastRead != null" class="in-progress-text">
              Last Read: {{ timestampDate(book.lastRead) }}
            </div>
          </div>
        </router-link>
      </div>
    </section>
  </main>
</template>

<script>
import AddBookForm from '../components/AddBookForm.vue';
import LoginDisplay from '../components/LoginDisplay.vue';

export default {
  name: "books",
  data() {
    return {
      showAddForm: false,
      isLoading: false,
      searchText: ""
    };
  },
  computed: {
    displayedBooks() {
      if (this.searchText != '') {
        return this.$store.state.books.filter( book => {
          return book.title.toLowerCase().includes(this.searchText.toLowerCase()) 
            || book.author.toLowerCase().includes(this.searchText.toLowerCase());
        });
      }
      return this.$store.state.books;
    }
  },
  methods: {
    timestampDate( timestamp ) {
      const parts = timestamp.split(/[T .]/);
      return parts[0];
    },
    bookImgSource(book) {
      return book.isbn == "" ? require('@/assets/book-image.jpg') : 'http://covers.openlibrary.org/b/isbn/' + book.isbn + '-M.jpg';
    }
  },
 
  components: {
    AddBookForm,
    LoginDisplay,
  },

  created() {
    this.$store.dispatch('retrieveBooks');
  },
};
</script>

<style scoped>
#book-display {
  display: flex;
  justify-content: space-evenly;
  flex-wrap: wrap;
}
button {
  margin-top: 1em;
  margin-bottom: 1em;
  padding: 0.6em;
  padding-left: 1.5em;
  padding-right: 1.5em;
  border: 2px solid black;
  border-radius: 8px;
  background-color:#31b4c9;
  font-weight: 800;
  color: white;
}
button:hover {
  background-color: #1590a3;
}

.book-panel {
  border: 2px solid black;
  border-radius: 8px;
  margin: 1em;
  padding: 0.2em;
  text-align: center;
  background-color: #eef2f3;
  box-shadow: 2px 3px #c9c9c9;
  font-weight: bold;
  width: 14em;
  height: auto;
}

.book-panel:hover {
  transform: scale(1.05);
  box-shadow: 5px 7px #c9c9c9;
}

.in-progress {
  background-color: paleturquoise;
}

.completed {
  background-color: palegreen;
}

img {
  height: 18em;
}

.book-title {
  margin: 1em;
  height: 1.5em;
  font-size: 1em;
}

.author {
  font-size: 0.75em;
  margin: 0.5em;
}

a {
  text-decoration: none;
  color: black;
}

.book-image {
  background-image: url("../assets/book-image.jpg");
  background-size: cover;
  max-width: 13em;
}

.book-search input {
  font-size: 1.5rem;
}
</style>