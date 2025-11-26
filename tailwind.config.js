// tailwind.config.js
module.exports = {
  content: [
    './src/main/resources/templates/**/*.html',
    // 例: もしJavaScriptでクラスを動的に操作している場合
    // './src/main/resources/static/js/**/*.js',
    // 例: もしJavaコードでHTML断片やクラス名を生成している場合（稀だが念のため）
    // './src/main/java/**/*.java',
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}