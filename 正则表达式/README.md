

[正则速查表](https://biaoyansu.com/28.cheatsheet)



\w：匹配大小写字母和数字和下划线_

\W：匹配被\w排除在外的字符

\d：匹配阿拉伯数字

\D：匹配被\d排除在外的字符

\s：匹配空白字符：空格、制表符、断行等

\S：匹配被\s排除在外的字符

[abc]或[a-c]：匹配存在abc其中一个的字符

\u：匹配Unicode编码，[\u4300-\u9fa5]匹配中文



[范围]+：重复一次或多次

[范围]*：出现零次或任意次(有或没有)

[范围]?：出现零次或一次

[范围]+?：+的懒惰模式

[范围]*?：\*的懒惰模式

[范围]??：?的懒惰模式



[范围]{x}：重复出现x次

[范围]{x,}：重复出现x次以上

[范围]{x,y}：重复出现x到y次



(模式)：分组匹配，摘取匹配到的内容

(?:模式)：分组匹配，不摘取匹配到的内容

```
1(?:37|38|82|83)\d{4}(\d{4})

13712241000
13822232000
18289993000
18398754000
13112241000
17722232000

$1:获取第一组
13712241000
13822232000
18289993000
18398754000
```

(?=模式)：正向肯定预查，匹配字符后面有该字符但不包含

(?!模式)：正向否定预查，匹配字符后面没有该字符

(?<=模式)：反向肯定预查，匹配字符前面有该字符但不包含

(?<!模式)：反向否定预查，匹配字符前面没有该字符



^：行首或字符串开始

$：行末或字符串结束

\b：边界结束

\B：非边界

