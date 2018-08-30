
export class StringUtils {
  /**
   * 判断是否空
   * @param obj
   * @returns {boolean}
   */
  public static isBlank(obj: any): boolean {
    return (!obj || obj.toString().trim() === "");
  }
}
