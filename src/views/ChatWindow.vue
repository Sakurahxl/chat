<template>
  <div id="chatWindow" ref="chat-window">
    <div style="position: absolute;width: 100%">{{this.contact.name}}({{this.contact.account}})</div>
    <div id="window-operation">
      <i class="el-icon-time" @click="displayHistory" title="查看历史记录"></i>
      <i class="el-icon-minus" @click="miniWin()" title="最小化"></i>
      <i v-if="judge" class="el-icon-full-screen" @click="maxWin()" title="全屏"></i>
      <i v-else class="el-icon-copy-document" @click="unMaxWin()" title="取消全屏"></i>
      <i class="el-icon-close" @click="closeWin()" title="关闭"></i>
    </div>
    <div id="chatWindow-record" ref="chatWindow-record">
      <div v-for="(val,index) in chats" :key='index'>
        <h5 style="margin: 0">{{ val.date }}</h5>
        <right-chat-box v-if="val.speaker==='0'" :theChat="val.contain" ref="right-chat-box" :user="user"/>
        <left-chat-box v-else :theChat="val.contain" ref="left-chat-box" :contact="contact"/>
      </div>
    </div>
    <div id="chatWindow-say">
      <richText  ref="rich-text" @getText="getText"/>
    </div>
  </div>
</template>

<script>
import RichText from '@/components/RichText'
import RightChatBox from '@/components/RightChatBox'
import LeftChatBox from '@/components/LeftChatBox'
export default {
  name: 'chatWindow',
  components: { LeftChatBox, RightChatBox, RichText },
  data () {
    return {
      judge: true,
      //是否已存入历史记录
      isHistory: false,
      //是否需要历史记录
      isNeed:false,
      //0是自己，1是对方
      historyChats: [],
      chats: [],
      contact: {
        account: "",
        name: "",
        avatar: ""
      },
      user: {
        account: "",
        name: "",
        avatar: ""
      },
      timer:""
    }
  },
  mounted () {
    this.getQueryVariable()
    //获取对方信息
    this.getData()
    this.getHistory()
    //设置定时器
    this.timer = setInterval(() => {
      this.getHistory()
    },1000)
  },
  methods: {
    displayHistory () {
      this.chats = []
      this.isNeed = !this.isNeed
      //获取历史信息
      this.getHistory()
    },
    miniWin () {
      const win = this.$remote.getCurrentWindow()
      win.minimize()
    },
    closeWin () {
      const win = this.$remote.getCurrentWindow()
      win.close()
    },
    maxWin () {
      const win = this.$remote.getCurrentWindow()
      win.maximize()
      this.judge = false
    },
    unMaxWin () {
      const win = this.$remote.getCurrentWindow()
      win.unmaximize()
      this.judge = true
    },
    //获取输入框内容
    getText (data) {
      this.getQueryVariable()
      this.putHistory(data)
      this.chats.push({speaker: '0', contain: data})
    },
    async putHistory(data) {
      const res = await this.$axios({
        url:"http://localhost:8866/addHistory",
        contentType: "application/x-www-form-urlencoded",
        params:{
          speak: this.user.account,
          hear: this.contact.account,
          say: data
        },
      }).then(res => {
        if (res.data !== '') {
          console.log(res.data)
        }
      })
    },
    async getHistory() {
      const res = await this.$axios({
        url:"http://localhost:8866/searchHistory",
        contentType: "application/x-www-form-urlencoded",
        params:{
          account1: this.user.account,
          account2: this.contact.account,
        },
      }).then(res => {
        if (res.data !== '') {
          this.chats = []
          for (let i = 0; i < res.data.length; i++) {
            let j;
            if (res.data[i].speak === this.user.account) {
              j = '0'
            }else {
              j = '1'
            }
            //判断是否已读
            if (res.data[i].isRead === 0 && !this.isHistory) {
              this.historyChats = this.chats
              this.isHistory = true;
            }
            this.chats.push({speaker: j, contain: res.data[i].say,date: res.data[i].date})
          }
          //得到历史记录
          if (!this.isHistory) {
            this.historyChats = this.chats
            this.isHistory = true;
            this.chats = null
          }else {
            if (!this.isNeed) {
              this.chats.splice(0,this.historyChats.length)
            }
          }
        }
      })
    },
    async getData() {
      const res = await this.$axios({
        url:"http://localhost:8866/login/searchContact",
        contentType: "application/x-www-form-urlencoded",
        params:{
          account: this.contact.account,
        },
      }).then(res => {
        if (res.data !== '') {
          this.contact.avatar = res.data.avatar
          this.contact.name = res.data.name
          this.contact.account = res.data.account
        }
      })
    },
    //获取url传参
    getQueryVariable() {
      const query = window.location.href
      const vars = query.split('?')
      const a  = vars[1].split(/=|&/)
      this.user.account = a[1]
      this.contact.account = a[3]
      }
    }
}
</script>

<style scoped>
#chatWindow {
  width: 100%;
  height: 20px;
  background-color: deepskyblue;
}

#chatWindow-say {
  -webkit-app-region: no-drag;
  position: absolute;
  width: 100%;
  top: 62%;
}

#chatWindow-record {
  position: absolute;
  -webkit-app-region: no-drag;
  top: 5.5%;
  width: 100%;
  height: 56%;
  z-index: 100;
  overflow:auto;
}

.el-icon-full-screen {
  -webkit-app-region: no-drag;
}

.el-icon-copy-document {
  -webkit-app-region: no-drag;
}

.el-icon-time {
  -webkit-app-region: no-drag;
}
</style>
