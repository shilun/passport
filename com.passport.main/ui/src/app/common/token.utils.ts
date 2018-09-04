/**
 * token 工具类
 */
export class TokenUtils {
  /**
   * 设置token
   * @param {string} token
   */
  public static upToken(token: string): void {
    Cookies.set('token', token, {expires: null, path: '/',domain:"jiahou.com"});
  }

  /**
   * 获取token
   * @returns {string}
   */
  public static getToken(): string {
    return Cookies.get('token');
  }
}
