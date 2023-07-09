<template>
  <div class="right-chatBox">
    <div class="right-chatMain">
      <a class="right-avatar"><img :src="circleUrl" id="theAvatar"></a>
      <div class="right-chat" ref="right-chat">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RightChatBox',
  props: {
    theChat: String,
    user: Object
  },
  mounted () {
    //获取头像转换成url形式
    this.getData()
    this.theChatDisplay()
  },
  data () {
    return {
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
    }
  },
  methods: {
    //放入内容
    theChatDisplay () {
      let tempNode = document.createElement('div')
      tempNode.innerHTML = this.theChat;
      tempNode = tempNode.firstChild
      this.$refs['right-chat'].appendChild(tempNode)
    },
    async getData() {
      const res = await this.$axios({
        url:"http://localhost:8866/login/searchContact",
        contentType: "application/x-www-form-urlencoded",
        params:{
          account: this.user.account,
        },
      }).then(res => {
        if (res.data !== '') {
          this.user.avatar = res.data.avatar
          //获取头像
          if (this.user.avatar == null || this.user.avatar === '') {
            this.circleUrl = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
          }else {
            this.circleUrl = require('@/assets/uploadImg/' + this.user.avatar)
          }
        }
      })
    },
  }
}
</script>

<style scoped>
.right-chatBox {
  display: inline-block;
  width: 100%;
}
.right-chatMain {
  float: right;
  display: inline-block;
  width: 400px;
}

.right-chat {
  float: right;
  display: inline-block;
  background-color: royalblue;
  padding-left: 10px;
  padding-right: 10px;
  max-width: 300px;
  word-wrap:break-word;
  margin-top: 2px;
  border-radius: 20px;
}

.right-avatar {
  margin: 2px;
  float: right;
}

#theAvatar {
  width: 45px;
  height: 45px;
  border: 1px dashed #d9d9d9;
  border-radius: 42px;
}
</style>
