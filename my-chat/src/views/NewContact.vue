<template>
  <div class="newContact">
    <div id="window-operation">
      <i class="el-icon-minus" @click="miniWin()"></i>
      <i class="el-icon-close" @click="closeWin()"></i>
    </div>
    <h3 class="newContact-text">请输入想要添加的联系人的账号</h3>
    <div class="newContact-search">
      <el-input placeholder="请输入内容" v-model="input1" class="input-with-select">
        <el-button slot="append" icon="el-icon-search" @click="getContactData"></el-button>
      </el-input>
    </div>
    <div class="newContact-result">
      <h1 v-if="judge">无搜索结果</h1>
      <div v-else class="result-contact" @click="confirmContact">
        <img :src="circleUrl" id="theAvatar" ref="theAvatar">
        <h2>name:&nbsp;&nbsp;{{this.contact.name}}</h2>
        <h4>account:&nbsp;&nbsp;{{this.contact.account}}</h4>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'newContact',
  data () {
    return {
      input1: '',
      judge: true,
      add:'',
      contact: {
        account: '',
        name: '',
        avatar: '',
        circleUrl: '',
      },
      user: {
        account:""
      }
    }
  },
  mounted () {
    this.getQueryVariable()
  },
  methods : {
    miniWin () {
      const win = this.$remote.getCurrentWindow()
      win.minimize()
    },
    closeWin () {
      const win = this.$remote.getCurrentWindow()
      win.close()
    },
    async getContactData() {
      const res = await this.$axios({
        url:"http://localhost:8866/login/searchContact",
        contentType: "application/x-www-form-urlencoded",
        params:{
          account: this.input1,
        },
      }).then(res => {
        if (res.data !== '') {
          this.contact.account = res.data.account
          this.contact.name = res.data.name
          if (res.data.avatar !== "" && res.data.avatar !== null) {
            this.contact.avatar = res.data.avatar
            this.circleUrl = require('@/assets/uploadImg/' + this.contact.avatar)
          }else {
            this.circleUrl = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
          }
          this.judge = false
        }else {
          this.judge = true
        }
      })
    },
    async confirmContact () {
      this.add = confirm("确认添加该用户？")
      if (this.add) {
        const res = await this.$axios({
          url: "http://localhost:8866/login/addContact",
          contentType: "application/x-www-form-urlencoded",
          params: {
            account1: this.user.account,
            account2: this.input1,
          },
        }).then(res => {
          if (res.data === "success") {
            console.log(res.data)
            alert('添加成功')
            this.$electron.ipcRenderer.send('window-refresh')
          }else if (res.data === "exist") {
            alert('已存在')
          }else {
            alert('添加失败')
          }
        })
      }
    },
    //获取url传参
    getQueryVariable() {
      const query = window.location.href
      const vars = query.split('?')
      const a  = vars[1].split(/=|&/)
      this.user.account = a[1]
    }
  }
}
</script>

<style scoped>
.newContact {
  background-color: royalblue;
  width: 100%;
  height: 300px;
}

.newContact-text {
  position: absolute;
  text-align: center;
  top: 10%;
  left: 25%;
}

.newContact-search {
  -webkit-app-region: no-drag;
  position: absolute;
  top: 30%;
  left: 25%;
}

.input-with-select  {
  background-color: #fff;
}

.newContact-result {
  position: absolute;
  width: 100%;
  height: 150px;
  top: 50%;
  background-color: white;
  text-align: center;
}

.result-contact {
  -webkit-app-region: no-drag;
  text-align: center;
  width: 100%;
  display: inline-block;
  z-index: 100;
}

.result-contact:hover {
  background-color: gray;
  cursor: pointer;
}

#theAvatar {
  width: 75px;
  height: 75px;
  left: 5%;
  top: 5%;
  border: 1px dashed #d9d9d9;
  border-radius: 42px;
  position: absolute;
  z-index: 100;
}
</style>
