module.exports = {
  root: true,
  parserOptions: {
    ecmaVersion: 2017,
    sourceType: 'module',
    ecmaFeatures: { jsx: true },
    parser: 'babel-eslint'
  },
  parser: 'vue-eslint-parser',
  extends: ['eslint:recommended', 'prettier'],
  plugins: ['prettier', 'vue'],
  rules: { 'prettier/prettier': ['error'] },
  env: { browser: true, node: true, es6: true, mocha: false, jest: false }
}
