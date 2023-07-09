<template>
  <div class="richText">
    <el-button id="richText-submit" type="primary" @click="submit()">发送</el-button>
    <el-button id="richText-empty" type="primary" @click="empty()">清空</el-button>
    <quill-editor
      ref="myQuillEditor"
      v-model="content"
      :options="editorOption"
      @blur="onEditorBlur($event)"
      @focus="onEditorFocus($event)"
      @ready="onEditorReady($event)"
    />
  </div>
</template>

<script>
// require styles 导入富文本编辑器对应的样式
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
import { quillEditor } from 'vue-quill-editor'

export default {
  name: 'RichText',
  data () {
    return {
      content: '',
      editorOption: {
        // Some Quill options...
        placeholder: '输入内容...',
        modules: {
          toolbar: {
            container: [
              ['bold', 'italic', 'underline', 'strike'],    //加粗，斜体，下划线，删除线
              ['blockquote', 'code-block'],     //引用，代码块
              [{ 'header': 1 }, { 'header': 2 }],        // 标题，键值对的形式；1、2表示字体大小
              [{ 'list': 'ordered' }, { 'list': 'bullet' }],     //列表
              [{ 'script': 'sub' }, { 'script': 'super' }],   // 上下标
              [{ 'indent': '-1' }, { 'indent': '+1' }],     // 缩进
              [{ 'size': ['small', false, 'large', 'huge'] }], // 字体大小
              [{ 'color': [] }, { 'background': [] }],     // 字体颜色，字体背景颜色
              [{ 'font': [] }],     //字体
              [{ 'align': [] }],    //对齐方式
              ['clean'],    //清除字体样式
              // ['image']    //上传图片
              ['image','video','link']    //上传图片、上传视频、上传文件
            ],
            handlers: {
              'image': function (value) {
                if (value) {
                  // 调用iview图片上传
                  document.querySelector('.upload-demo .ivu-btn').click()
                } else {
                  this.quill.format('image', false)
                }
              }
            }
          }
        }
      }
    }
  },
  methods: {
    onEditorBlur (quill) {
      console.log('editor blur!', quill)
    },
    onEditorFocus (quill) {
      console.log('editor focus!', quill)
    },
    onEditorReady (quill) {
      console.log('editor ready!', quill)
    },
    empty () {
      this.content = ''
    },
    maxRich () {
      console.log(this.$refs['myQuillEditor'])
    },
    unMaxRich () {
      // this.$refs['myQuillEditor'].style.height = '150px'
    },
    submit(){
      this.$emit('getText',this.content);
      this.content = ''
    },
  },
  computed: {
    editor () {
      return this.$refs.myQuillEditor.quill
    }
  },
  mounted () {
    console.log('this is current quill instance object', this.editor)
  },
  components: {
    quillEditor
  }
}
</script>

<style scoped>
.quill-editor {
  height: 162px;
}



#richText-submit {
  position: absolute;
  z-index: 100;
  right: 10px;
  line-height: 5px;
  bottom: -64px;
}

#richText-empty {
  position: absolute;
  z-index: 100;
  right: 100px;
  line-height: 5px;
  bottom: -64px;
}
</style>
