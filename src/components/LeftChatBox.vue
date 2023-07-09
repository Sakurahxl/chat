<template>
  <div class="left-chatBox">
    <div class="left-chatMain">
      <a class="left-avatar"><img :src="circleUrl" id="theAvatar"></a>
      <div class="left-chat" ref="left-chat">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LeftChatBox',
  data () {
    return {
      circleUrl: "",
    }
  },
  props: {
    theChat: String,
    contact: Object
  },
  mounted () {
    //获取头像
    if (this.contact.avatar == null || this.contact.avatar === '') {
      this.circleUrl = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    }else {
      this.circleUrl = require('@/assets/uploadImg/' + this.contact.avatar)
    }
    this.theChatDisplay()
  },
  methods:{
    theChatDisplay () {
      let tempNode = document.createElement('div')
      tempNode.innerHTML = this.theChat;
      tempNode = tempNode.firstChild
      this.$refs['left-chat'].appendChild(tempNode)
    }
  }
}
</script>

<style scoped>
.left-chatBox {
  text-align: left;
  display: inline-block;
  width: 100%;
}
.left-chatMain {
  display: inline-block;
  width: 400px;
}

.left-chat {
  float: left;
  display: inline-block;
  background-color: royalblue;
  padding-left: 10px;
  padding-right: 10px;
  max-width: 300px;
  word-wrap:break-word;
  margin-top: 2px;
  border-radius: 20px;
}

.left-avatar {
  margin: 2px;
  float: left;
}

#theAvatar {
  width: 45px;
  height: 45px;
  border: 1px dashed #d9d9d9;
  border-radius: 42px;
}
</style>
